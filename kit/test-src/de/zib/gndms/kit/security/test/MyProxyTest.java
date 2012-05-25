package de.zib.gndms.kit.security.test;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.zib.gndms.kit.access.MyProxyClient;
import org.globus.gsi.GlobusCredentialException;
import org.globus.myproxy.MyProxyException;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.io.IOException;


/**
 * @author try ma ik jo rr a zib
 * @date 18.04.11  16:14
 * @brief
 */
public class MyProxyTest extends MyProxyTestBase {


    public static void main( String[] args ) throws Exception {
        (new MyProxyTest() ).run( args );
        System.exit( 0 );
    }


    @Override
    public void fetch( ) throws GSSException, GlobusCredentialException, MyProxyException, IOException {

        // connectionCredential can by any certificate accepted by the
        // myproxy-server (user, container or host cert doesn't
        // matter) Except for certs initialized with the -Z option:
        // Then it is used for passwordless cert retrieval, the
        // cert itself must be accepted by the myproxy-server and the
        // DN either (CN or regex) must match the trusted retrieves
        // policy of the server
        GSSCredential connectionCredential = null;

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

        MyProxyClient myProxyClient = new MyProxyClient( getMyProxy() );
        myProxyClient.setConnectionCredential( connectionCredential );


        GSSCredential newCred = myProxyClient.retrieve( username, "myproxy20testpass12" );


        String path = credentialToFile( ( ExtendedGSSCredential ) newCred );


        System.out.println("A cert has been received for user " +
            username + " in " + path);
    }


}
