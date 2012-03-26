/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.common.kit.security;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * @date: 26.03.12
 * @time: 14:59
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SetupSSLFactory {
    private final String keyStoreLocation;
    private final String trustStoreLocation;
    private final String keyStorePassword;
    private final String trustStorePassword;
    private final String keyPassword;

    public SetupSSLFactory(
            final String trustStoreLocation,
            final String trustStorePassword,
            final String keyStoreLocation,
            final String keyStorePassword,
            final String keyPassword ) {
        this.keyStoreLocation = keyStoreLocation;
        this.trustStoreLocation = trustStoreLocation;
        this.keyStorePassword = keyStorePassword;
        this.trustStorePassword = trustStorePassword;
        this.keyPassword = keyPassword;
    }

    public SetupSSLFactory(
            final String keyStoreLocation,
            final String keyStorePassword,
            final String keyPassword ) {
        this.keyStoreLocation = keyStoreLocation;
        this.trustStoreLocation = null;
        this.keyStorePassword = keyStorePassword;
        this.trustStorePassword = null;
        this.keyPassword = keyPassword;
    }

    public SetupSSL getInstance()
            throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException,
            UnrecoverableKeyException
    {
        SetupSSL setupSSL = new SetupSSL();

        if( null != trustStoreLocation ) {
            setupSSL.setTrustStoreLocation( trustStoreLocation );
            setupSSL.prepareTrustStore( trustStorePassword, "JKS" );
        }
        setupSSL.setKeyStoreLocation( keyStoreLocation );
        setupSSL.prepareKeyStore( keyStorePassword, keyPassword, "JKS" );

        return setupSSL;
    }
}
