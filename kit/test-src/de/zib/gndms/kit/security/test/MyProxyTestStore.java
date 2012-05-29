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

import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.globus.myproxy.StoreParams;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 25.05.12  16:38
 * @brief
 */
public class MyProxyTestStore extends MyProxyTestBase {


    @Option( name="-y", usage="file containing the cert to store", required = true)
    protected String privateKeyFile;

    @Option( name="-pass", usage="store pass-phrase", required = true)
    protected String storePassphrase;

    @Option( name="-pkpass", usage="pass-phrase for encrypted rsa-key. (ignored if the key isn't " +
                                   "" +
                                   "encrypted" )
    protected String pkPassphrase;


    public static void main( String[] args ) throws Exception {
        (new MyProxyTestStore() ).run( args );
        System.exit( 0 );
    }

    @Override
    public void fetch()
            throws GSSException, GlobusCredentialException, MyProxyException, IOException,
            GeneralSecurityException {

        GSSCredential connectionCredential;
        if( true ) {
            // connectionCredential = findCredential( credentialFilename );
            connectionCredential = findCredential( null );

            // Below load host cert from for standard /etc/grid-security/host{key,cert}.pem
            // String base = "/home/mjorra/Creations/ZIB/GridZertifikate/2011/host_csr-pc35";
            // connectionCredential =  new GlobusGSSCredentialImpl(
            //              new GlobusCredential( base + "/hostcert.pem", base + "/hostkey.pem" ),
            //              GSSCredential.INITIATE_AND_ACCEPT );
            //
            System.out.println( "using connection Cert " + connectionCredential.getName() );
        }

        final MyProxy myProxy = getMyProxy();

        StoreParams storeParams = new StoreParams();
        storeParams.setUserName( username );
        //storeParams.setPassphrase( storePassphrase );


        CertStuffHolder certStuff = PemReaderTest.readKeyPair( new File( privateKeyFile ),
                pkPassphrase.toCharArray() );

        final BouncyCastleOpenSSLKey openSSLKey = new BouncyCastleOpenSSLKey( certStuff.getKeyPair()
                .getPrivate() );
        openSSLKey.encrypt( storePassphrase.getBytes() );
        myProxy.store( connectionCredential, certStuff.getChain().toArray( new
                X509Certificate[1] ), openSSLKey, storeParams );

    }
}
