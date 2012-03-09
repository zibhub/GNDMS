/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.kit.access;

import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.auth.HostAuthorization;
import org.globus.gsi.gssapi.auth.NoAuthorization;
import org.globus.myproxy.MyProxy;
import org.globus.util.Util;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  17:29
 * @brief
 */
public class MyProxyFactory {

    private final String nickname; ///< Nickname of this provider should have something to do with its purpose.

    public static final int DEFAULT_LIFETIME = 24; // lifetime hours
    private String subjectDN; // dn of the cert issuer
    private String myProxyServer = "localhost"; // host of the myproxy server
    private int port = 7512; // port of the myproxy server
    private int lifetime = DEFAULT_LIFETIME; // cert lifetime in hours
    private boolean noPasswd=false; // try passwordless login

    private CredentialHolder connectionCredentialHolder; // holds credentials for connections


    public MyProxyFactory( String nickname ) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }


    // the authorization method of the target machine
    // either an expected SUBJECT or a valid HOST-certificate
    public enum Authorization { SUBJECT, HOST }
    private Authorization authMethod = Authorization.HOST;




    public MyProxyClient getMyProxyClient() throws GSSException, GlobusCredentialException {

        MyProxyClient myProxyClient =  new MyProxyClient( getMyProxy() );
        myProxyClient.setConnectionCredential( connectionCredentialHolder.getCredential() );
        myProxyClient.setLifetime( lifetime );
        return myProxyClient;
    }


    protected MyProxy getMyProxy() {

        MyProxy myProxy = new MyProxy( myProxyServer, port );

        switch ( authMethod ) {
            case SUBJECT:
                myProxy.setAuthorization(new NoAuthorization() );
                break;
            case HOST:
                myProxy.setAuthorization( HostAuthorization.getInstance() );
                break;
        }

        return myProxy;
    }


    public String getSubjectDN() {
        return subjectDN;
    }


    public void setSubjectDN( String subjectDN ) {
        this.subjectDN = subjectDN;
    }


    public String getMyProxyServer() {
        return myProxyServer;
    }


    public void setMyProxyServer( String myProxyServer ) {
        this.myProxyServer = myProxyServer;
    }


    public int getPort() {
        return port;
    }


    public void setPort( int port ) {
        this.port = port;
    }


    public int getLifetime() {
        return lifetime;
    }


    public void setLifetime( int lifetime ) {
        this.lifetime = lifetime;
    }


    public boolean isNoPasswd() {
        return noPasswd;
    }


    public void setNoPasswd( boolean noPasswd ) {
        this.noPasswd = noPasswd;
    }


    public CredentialHolder getConnectionCredentialHolder() {
        return connectionCredentialHolder;
    }


    public void setConnectionCredentialHolder( CredentialHolder connectionCredentialHolder ) {
        this.connectionCredentialHolder = connectionCredentialHolder;
    }


    public Authorization getAuthMethod() {
        return authMethod;
    }


    public void setAuthMethod( Authorization authMethod ) {
        this.authMethod = authMethod;
    }


    public static String credentialToFile( GSSCredential newCred, String outputFile ) throws IOException, GSSException {

        File f;
        if (outputFile != null) {
            f = new File(outputFile);
        } else {
            f = File.createTempFile("x509up_", ".pem", new File("/tmp"));
        }
        String path = f.getPath();

        OutputStream out = null;
        try {
            out = new FileOutputStream(path);

            // set read only permissions
            Util.setOwnerAccessOnly( path );

            // write the contents
            byte [] data =
                (( ExtendedGSSCredential )newCred).export( ExtendedGSSCredential.IMPEXP_OPAQUE );

            out.write(data);
        } finally {
            if (out != null) {
                try { out.close(); } catch(Exception e) { e.printStackTrace(); }
            }
        }
        return path;
    }


}
