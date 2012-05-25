package de.zib.gndms.kit.security.test;
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

import de.zib.gndms.common.kit.application.AbstractApplication;
import de.zib.gndms.kit.access.myproxyext.ExtMyProxy;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.gssapi.auth.HostAuthorization;
import org.globus.gsi.gssapi.auth.IdentityAuthorization;
import org.globus.gsi.gssapi.auth.NoAuthorization;
import org.globus.myproxy.MyProxyException;
import org.globus.util.Util;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 25.05.12  15:51
 * @brief
 */
public abstract class MyProxyTestBase extends AbstractApplication {

    public static final int DEFAULT_LIFETIME = 24; // lifetime hours
    @Option( name ="-dn" )
    protected String subjectDN; // dn of the cert issuer
    @Option( name ="-host" )
    protected String host = "localhost"; // host of the myproxy server
    @Option( name ="-port" )
    protected int port = 7512; // port of the myproxy server
    @Option( name ="-credn" )
    protected String credentialName; // myproxy credential name, if required
    @Option( name ="-lt" )
    protected int lifetime; // cert lifetime in hours
    @Option( name="-nopass" )
    protected boolean noPasswd=false; // try passwordless login
    @Option( name ="-usr" )
    protected String username; // myproxy username
    @Option( name ="-proxy" )
    protected String credentialFilename; // we use credential based myproxy login
    @Option( name ="-dest" )
    protected String outputFile=null; // output of the retrieved cert
    private Authorization authMethod = Authorization.NONE;


    public static void main( String[] args ) throws Exception {
        (new MyProxyTest() ).run( args );
        System.exit( 0 );
    }


    @Override
    public void run() throws Exception {
        fetch();
    }


    protected ExtMyProxy getMyProxy() {
        ExtMyProxy myProxy = new ExtMyProxy( host, port );

        switch ( authMethod ) {
            case SUBJECT:
                myProxy.setAuthorization(new IdentityAuthorization( subjectDN ) );
                break;
            case HOST:
                myProxy.setAuthorization( HostAuthorization.getInstance() );
                break;
            case NONE:
                myProxy.setAuthorization( new NoAuthorization() );
                break;
        }

        return myProxy;
    }


    public abstract void fetch() throws GSSException, GlobusCredentialException, MyProxyException,
            IOException, GeneralSecurityException;


    public String credentialToFile( ExtendedGSSCredential newCred ) throws IOException, GSSException {

        File f = null;
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
                ((ExtendedGSSCredential )newCred).export( ExtendedGSSCredential.IMPEXP_OPAQUE );

            out.write(data);
        } finally {
            if (out != null) {
                try { out.close(); } catch(Exception e) { e.printStackTrace(); }
            }
        }
        return path;
    }


    protected GSSCredential findCredential( String credentialFilename ) throws GlobusCredentialException, GSSException {

       GlobusCredential credential;
       if ( credentialFilename == null ) {
           credential = GlobusCredential.getDefaultCredential();
       } else {
           credential = new GlobusCredential( credentialFilename );
       }
       return new GlobusGSSCredentialImpl( credential, GSSCredential.INITIATE_AND_ACCEPT );
   }


    // the authorization method of the target machine
    // either an expected SUBJECT or a valid HOST-certificate
    enum Authorization { SUBJECT, HOST, NONE }
}
