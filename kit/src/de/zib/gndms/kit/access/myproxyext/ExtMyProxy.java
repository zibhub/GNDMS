package de.zib.gndms.kit.access.myproxyext;


/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.globus.common.CoGProperties;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.gssapi.GSSConstants;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.gssapi.net.GssSocket;
import org.globus.gsi.gssapi.net.GssSocketFactory;
import org.globus.myproxy.GetParams;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyConstants;
import org.globus.myproxy.MyProxyException;
import org.globus.util.Base64;
import org.gridforum.jgss.ExtendedGSSManager;
import org.ietf.jgss.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

/**
 * @author Marcel Bardenhagen
 * @email Marcel.Bardenhagen@awi.de
 * @date 14.05.2012
 */
public class ExtMyProxy extends MyProxy{
    
    protected static final Logger logger = LoggerFactory.getLogger( ExtMyProxy.class );
    
    private static final String RESPONSE   = "RESPONSE=";
    private static final String ERROR      = "ERROR=";
    private static final String AUTHZ_DATA = "AUTHORIZATION_DATA=";
    
    private static final String TRUSTROOTS      = "TRUSTED_CERTS=";

    public ExtMyProxy(String host, int port) {
        super(host, port);
    }
    
    private GssSocket getSocket(GSSCredential credential) 
        throws IOException, GSSException {
        GSSManager manager = ExtendedGSSManager.getInstance();
        
        this.context = manager.createContext(null, 
                                             GSSConstants.MECH_OID,
                                             credential,
                                             GSSContext.DEFAULT_LIFETIME);

        // no delegation
        this.context.requestCredDeleg(false);

        IOException exception = null;
        Socket socket = null;
        String goodAddr = "";
        int hostIdx = 0;
        String hosts[] = host.split(",");
        int socketTimeout = CoGProperties.getDefault().getSocketTimeout();

        search:
            while (hostIdx < hosts.length) {
                InetAddress addrs[] = InetAddress.getAllByName(hosts[hostIdx]);
                for (int addrIdx = 0; addrIdx < addrs.length; addrIdx++) {
                    exception = null;
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug("getSocket(): Trying " + 
                                addrs[addrIdx].toString());
                        }
                        socket = new Socket();
                        socket.connect(
                            new InetSocketAddress(addrs[addrIdx],port), 
                            socketTimeout*1000); // seconds to milliseconds

                        goodAddr = addrs[addrIdx].toString();
                        if (logger.isDebugEnabled()) {
                            logger.debug("             Succeeded.");
                        }
                        break search;
                    } catch (IOException e) {
                        exception = e;
                        if (logger.isDebugEnabled()) {
                            logger.debug("             Failed.");
                        }
                    } 
                }
                hostIdx += 1;
            }

        if (exception != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("getSocket(): " +
                    "Unable to connect to a MyProxy host");
            }
            throw exception;
        }
        
        GssSocketFactory gssFactory = GssSocketFactory.getDefault();

        GssSocket gssSocket = 
            (GssSocket)gssFactory.createSocket(socket,
                                               hosts[hostIdx], 
                                               port, 
                                               this.context);

        if (logger.isDebugEnabled()) {
            logger.debug("getSocket(): Connected to " + goodAddr);
        }

        gssSocket.setAuthorization(this.authorization);

        return gssSocket;
    }
    
    /**
     * Retrieves delegated credentials from the MyProxy server.
     *
     * @param  credential 
     *         The local GSI credentials to use for authentication.
     *         Can be set to null if no local credentials.
     * @param  params
     *         The parameters for the get operation.
     * @return GSSCredential 
     *         The retrieved delegated credentials.
     * @exception MyProxyException
     *         If an error occurred during the operation.
     */
    public GSSCredential retrieve(GSSCredential credential,
                             final GetParams params)
        throws MyProxyException {
         
        if (params == null) {
            throw new IllegalArgumentException("params == null");
        }

        if (credential == null) {
            try {
                credential = getAnonymousCredential();
            } catch (GSSException e) {
                throw new MyProxyException("Failed to create anonymous credentials", e);
            }
        }

        String msg = params.makeRequest();

        Socket gsiSocket = null;
        OutputStream out = null;
        InputStream in   = null;

        try {
            gsiSocket = getSocket(credential);

            if (credential.getName().isAnonymous()) {
                this.context.requestAnonymity(true);
            }

            out = gsiSocket.getOutputStream();
            in  = gsiSocket.getInputStream();

            // send message
            out.write(msg.getBytes());
            out.flush();

            if (logger.isDebugEnabled()) {
                logger.debug("Req sent:" + params);
            }

            // may require authz handshake / without trustedroots
            handleReply(in, out, params.getAuthzCreds(), false);

            // start delegation - generate key pair
            KeyPair keyPair = CertUtil.generateKeyPair("RSA", DEFAULT_KEYBITS);

            BouncyCastleCertProcessingFactory certFactory =
                BouncyCastleCertProcessingFactory.getDefault();

            byte [] req = null;
            if (credential.getName().isAnonymous()) {
                req = certFactory.createCertificateRequest("CN=ignore",
                                                           keyPair);
            } else {
                GlobusGSSCredentialImpl pkiCred = 
                    (GlobusGSSCredentialImpl)credential;
                req = certFactory.createCertificateRequest(pkiCred.getCertificateChain()[0],
                                                           keyPair);
            }

            // send the request to server
            out.write(req);
            out.flush();
            
            // read the number of certificates
            // int size = in.read();

            // if (logger.isDebugEnabled()) {
            //     logger.debug("Reading " + size + " certs");
            // }

            // X509Certificate [] chain = new X509Certificate[size];

            // ---------- CUSTOM PART START ----------
            ArrayList<X509Certificate> chain = new ArrayList<X509Certificate>(1);

            PEMReader pemReader = new PEMReader(new InputStreamReader(in),
                    new PasswordFinder() {
                        @Override
                        public char[] getPassword() {

                            return params.getPassphrase().toCharArray();
                            //return new char[0];  // not required here
                        }
                    } );

            Object obj;
            while ((obj = pemReader.readObject()) != null) {
                if (obj instanceof X509Certificate ) {
                    X509Certificate cert = ( X509Certificate ) obj;
                    System.out.println( "read cert: " + cert.toString() );
                    chain.add( cert );
                }
                if (obj instanceof KeyPair ) {
                    keyPair = ( KeyPair ) obj;
                    System.out.println( "read keypair: " + keyPair.toString() );
                    System.out.println( "read keypair: " + keyPair.getPrivate() );
                    System.out.println( "read keypair: " + keyPair.getPublic() );
                }
            }

            // ---------- CUSTOM PART END ----------
//
//            for (int i=0;i<size;i++) {
//                chain[i] = certFactory.loadCertificate(in);
//                System.out.println("Received cert: " + chain[i].getSubjectDN());
//                // DEBUG: display the cert names
//                if (logger.isDebugEnabled()) {
//                    logger.debug("Received cert: " + chain[i].getSubjectDN());
//                }
//            }
//
//            // get the response
//            handleReply(in);
//
            // make sure the private key belongs to the right public key
            // currently only works with RSA keys
            RSAPublicKey pkey   = (RSAPublicKey)chain.get( 0 ).getPublicKey();
            RSAPrivateKey prkey = (RSAPrivateKey)keyPair.getPrivate();

            if (!pkey.getModulus().equals(prkey.getModulus())) {
                throw new MyProxyException("Private/Public key mismatch!");
            }
            
            GlobusCredential newCredential = null;
            
            newCredential = new GlobusCredential(keyPair.getPrivate(),
                    chain.toArray( new X509Certificate[chain.size()]) );

            return new GlobusGSSCredentialImpl(newCredential, GSSCredential.INITIATE_AND_ACCEPT);
            
        } catch(Exception e) {
            throw new MyProxyException("MyProxy get failed.", e);
        } finally {
            // close socket
            close(out, in, gsiSocket);
        }
    }
    
    
        /**
     * Nachträglich hinzugefügt Methode
     * @param in
     * @return
     * @throws IOException 
     */
    private String getStringFromInputStream(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }
    
    public static InputStream getInputStreamFromString(String str) throws UnsupportedEncodingException
    {
        byte[] bytes = str.getBytes("UTF-8");
        return new ByteArrayInputStream(bytes);
    }
    
    private GSSCredential getAnonymousCredential() 
        throws GSSException {
        GSSManager manager = ExtendedGSSManager.getInstance();
        GSSName anonName = manager.createName((String)null, null);
        return manager.createCredential(anonName,
                                        GSSCredential.INDEFINITE_LIFETIME,
                                        (Oid)null,
                                        GSSCredential.INITIATE_AND_ACCEPT);
    }
    
    
    /**
     * Nachträglich hinzugefügt Methode
     * @param in
     * @return
     * @throws IOException 
     */
    private String inputStreamToString(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }
    
    private InputStream handleReply(InputStream in)
        throws IOException, MyProxyException {
        return handleReply(in, null, null, false);
    }
    
    private InputStream handleReply(InputStream in, 
                                    OutputStream out,
                                    GSSCredential authzcreds,
                                    boolean wantTrustroots)
        throws IOException, MyProxyException {
        String tmp = null;

        /* there was something weird here with the 
           received protocol version sometimes. it
           contains an extra <32 byte. fixed it by
           using endsWith. now i read extra byte at the
           end of each message.
        */

        // protocol version
        tmp = readLine(in);
        if (tmp == null) {
            throw new EOFException();
        }
        if (!tmp.endsWith(MyProxyConstants.VERSION)) {
            throw new MyProxyException("Protocol version mismatch: " + tmp);
        }

        // response
        tmp = readLine(in);
        if (tmp == null) {
            throw new EOFException();
        }

        if (!tmp.startsWith(RESPONSE)) {
            throw new MyProxyException("Invalid reply: no response message");
        }

        boolean error = tmp.charAt(RESPONSE.length()) == '1';
        boolean authzchallenge = tmp.charAt(RESPONSE.length()) == '2';

        if (error) {
            StringBuffer errorStr = new StringBuffer();
            while( (tmp = readLine(in)) != null ) {
                if (tmp.startsWith(ERROR)) {
                    if (errorStr.length() > 0) errorStr.append(' ');
                    errorStr.append(tmp.substring(ERROR.length()));
                }
            }
            if (errorStr.length() == 0) {
                errorStr.append("unspecified server error");
            }
            throw new MyProxyException(errorStr.toString());
        }
        if (authzchallenge) {
            if (authzcreds == null) {
                throw new MyProxyException("Unable to respond to server's authentication challenge. No credentials for renewal.");
            }
            if (out == null) {
                throw new MyProxyException("Internal error. Authz challenge but no OutputStream.");
            }
            String[] authzdata = null;
            while( (tmp = readLine(in)) != null ) {
                if (tmp.startsWith(AUTHZ_DATA)) {
                    int pos = tmp.indexOf(':', AUTHZ_DATA.length()+1);
                    if (pos != -1) {
                        authzdata = new String[2];
                        authzdata[0] = 
                            tmp.substring(AUTHZ_DATA.length(), pos).trim();
                        authzdata[1] =
                            tmp.substring(pos+1).trim();
                    }
                }
            }
            if (authzdata == null) {
                throw new MyProxyException("Unable to parse authorization challenge from server.");
            }
            if (authzdata[0].equals("X509_certificate")) {
                GlobusGSSCredentialImpl pkiCred =
                    (GlobusGSSCredentialImpl)authzcreds;
                try {
                    Signature sig = Signature.getInstance("SHA1withRSA");
                    sig.initSign(pkiCred.getPrivateKey());
                    sig.update(authzdata[1].getBytes());
                    byte[] sigbytes = sig.sign();
                    X509Certificate [] certs =
                        pkiCred.getCertificateChain();
                    ByteArrayOutputStream buffer =
                        new ByteArrayOutputStream(2048);
                    buffer.write(2); // AUTHORIZETYPE_CERT
                    buffer.write(0); buffer.write(0); buffer.write(0); // pad
                    buffer.write(0); buffer.write(0); buffer.write(0); // pad
                    buffer.write(sigbytes.length);
                    buffer.write(sigbytes);
                    buffer.write((byte)certs.length);
                    for (int i=0; i<certs.length; i++) {
                        buffer.write(certs[i].getEncoded());
                    }
                    out.write(buffer.toByteArray());
                    out.flush();
                } catch(Exception e) {
                    throw new MyProxyException("Authz response failed.", e);
                }
                return handleReply(in, out, authzcreds, wantTrustroots);
            } else {
                throw new MyProxyException("Unable to respond to server's authentication challenge. Unimplemented method: " + authzdata[0]);
            }
        }

        if (wantTrustroots == true) {
            while ((tmp = readLine(in)) != null) {
                if (tmp.startsWith(TRUSTROOTS)) {
                    String filenameList = tmp.substring(TRUSTROOTS.length());
                    this.trustrootFilenames = filenameList.split(",");
                    this.trustrootData = new String[this.trustrootFilenames.length];
                    for(int i = 0; i < this.trustrootFilenames.length; i++) {
                    String lineStart = "FILEDATA_" + this.trustrootFilenames[i]
                        + "=";
                    tmp = readLine(in);
                    if (tmp == null) {
                        throw new EOFException();
                    }
                    if (!tmp.startsWith(lineStart)) {
                        throw new MyProxyException(
                            "bad MyProxy protocol RESPONSE: expecting "
                            + lineStart + " but received " + tmp);
                    }
                    this.trustrootData[i] = new String(
                        Base64.decode(tmp.substring(lineStart.length()).getBytes()));
                    }
                }
            }
        }
        
        /* always consume the entire message */
        int avail = in.available();
        byte [] b = new byte[avail];
        if (avail > 0) in.read(b);

        ByteArrayInputStream inn = new ByteArrayInputStream(b);

        return inn;
    }
    
    private static String readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int c = is.read(); c > 0 && c != '\n'; c = is.read()) {
            sb.append((char) c);
        }
        if (sb.length() > 0) {
            logger.debug("Received line: " + sb);
            return new String(sb);
        }
        return null;
    }
    
    private static void close(OutputStream out, 
                              InputStream in,
                              Socket sock) {
        try {
            if (out != null) out.close();
            if (in  != null) in.close();
            if (sock != null) sock.close();
        } catch(IOException ee) {}
    }
    
}
