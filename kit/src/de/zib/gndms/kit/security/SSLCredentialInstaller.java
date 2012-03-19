package de.zib.gndms.kit.security;
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

import de.zib.gndms.common.kit.security.SetupSSL;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;

import java.security.KeyStoreException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 16.03.12  11:36
 * @brief
 */
public class SSLCredentialInstaller extends GSSCredentialInstaller<SSLCredentialInstaller .InstallerParams> {

    public SSLCredentialInstaller() {
        super( InstallerParams.class );
    }

    @Override
    public void installCredentials( final InstallerParams credentialReceiver,
                                    final GSSCredential cred ) {
        if( GlobusGSSCredentialImpl.class.isInstance( cred ) ) {
            GlobusGSSCredentialImpl gssCredential = GlobusGSSCredentialImpl.class.cast( cred );

            final SetupSSL setupSSL = credentialReceiver.getSetupSSL();
            try {
                setupSSL.addCertificate( gssCredential.getPrivateKey(),
                        gssCredential.getCertificateChain(),
                        credentialReceiver.getAlias(),
                        credentialReceiver.getPassword() );
            } catch ( KeyStoreException e ) {
                throw new RuntimeException( e );
            }
        }
    }


    public static class InstallerParams {
        
        private String alias;
        private String password;
        private SetupSSL setupSSL;


        public String getAlias() {

            return alias;
        }


        public InstallerParams setAlias( final String alias ) {

            this.alias = alias;
            return this;
        }


        public String getPassword() {

            return password;
        }


        public InstallerParams setPassword( final String password ) {

            this.password = password;
            return this;
        }


        public SetupSSL getSetupSSL() {

            return setupSSL;
        }


        public InstallerParams setSetupSSL( final SetupSSL setupSSL ) {

            this.setupSSL = setupSSL;
            return this;
        }
    }
}
