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

package de.zib.gndms.kit.security;

import de.zib.gndms.kit.access.MyProxyProvider;
import org.ietf.jgss.GSSCredential;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  17:21
 * @brief
 */
public class MyProxyCredentialProvider extends GSSCredentialProviderImpl {

    private String user;
    private String passwd;
    private final MyProxyProvider provider;


    public MyProxyCredentialProvider( MyProxyProvider provider ) {
        super();
        this.provider = provider;
    }


    public MyProxyCredentialProvider( MyProxyProvider provider, String user, String passwd ) {

        this.user = user;
        this.passwd = passwd;
        this.provider = provider;
    }


    @Override
    public GSSCredential getCredential() {
        GSSCredential cred = super.getCredential();
        if ( cred == null ) {
            try {
                cred = provider.getMyProxyClient().fetch( user, passwd );
                setCredential( cred );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }
        }
        return cred;
    }


    public String getUser() {
        return user;
    }


    public void setUser( String user ) {
        this.user = user;
    }
}
