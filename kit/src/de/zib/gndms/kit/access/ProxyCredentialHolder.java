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

import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  18:36
 * @brief
 */
public class ProxyCredentialHolder implements CredentialHolder {

    final private String credentialFileName;
    private GSSCredential credential = null;


    public ProxyCredentialHolder( String credentialFileName ) {
        this.credentialFileName = credentialFileName;
    }


    @Override
    public GSSCredential getCredential() throws GlobusCredentialException, GSSException {
        if( credential == null )
            credential = loadCredential( credentialFileName );
        return credential;
    }

    public static GSSCredential loadCredential( String credentialFilename ) throws GlobusCredentialException, GSSException {

        GlobusCredential credential;
        if ( credentialFilename == null ) {
            credential = GlobusCredential.getDefaultCredential();
        } else {
            credential = new GlobusCredential( credentialFilename );
        }
        return new GlobusGSSCredentialImpl( credential, GSSCredential.INITIATE_AND_ACCEPT );
    }
}
