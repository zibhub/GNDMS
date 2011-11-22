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
import org.globus.myproxy.GetParams;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.io.IOException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  17:30
 * @brief
 */
public class MyProxyClient {

    private MyProxy myProxy;

    // connectionCredention can by any certificate accepted by the
    // myproxy-server (user, container or host cert doesn't
    // matter) Except for certs initialized with the -Z option:
    // Then it is used for passwordless cert retrieval, the
    // cert itself must be accepted by the myproxy-server and the
    // DN either (CN or regex) must match the trusted retrieves
    // policy of the server.
    private GSSCredential connectionCredential;

    private String credentialName; ///< Name of the stored credential. May be null
    private int lifetime; ///< Credential lifetime


    public MyProxyClient( MyProxy myProxy ) {
        this.myProxy = myProxy;
    }


    /**
     * Fetches Credentials using login/password pair to access the myproxy
     * @param login
     * @param passwd
     * @throws org.ietf.jgss.GSSException
     * @throws org.globus.gsi.GlobusCredentialException
     * @throws org.globus.myproxy.MyProxyException
     * @throws java.io.IOException
     */
    public GSSCredential fetch( String login, String passwd ) throws GSSException, GlobusCredentialException, MyProxyException, IOException {


        System.out.println( "using connection Cert " + connectionCredential.getName() );

        // this is the get request for the myproxy server
        GetParams getRequest = new GetParams();
        getRequest.setUserName( login );
        getRequest.setPassphrase( passwd );
        getRequest.setCredentialName( credentialName );
        getRequest.setLifetime( lifetime * 3600 );

        return myProxy.get( connectionCredential, getRequest );
    }


    public GSSCredential getConnectionCredential() {
        return connectionCredential;
    }


    public void setConnectionCredential( GSSCredential connectionCredential ) {
        this.connectionCredential = connectionCredential;
    }


    public String getCredentialName() {
        return credentialName;
    }


    public void setCredentialName( String credentialName ) {
        this.credentialName = credentialName;
    }


    public int getLifetime() {
        return lifetime;
    }


    public void setLifetime( int lifetime ) {
        this.lifetime = lifetime;
    }

}
