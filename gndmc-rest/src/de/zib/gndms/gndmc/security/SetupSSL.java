package de.zib.gndms.gndmc.security;
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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 05.03.12  16:52
 * @brief
 */
public class SetupSSL {

    public static final String TRUST_STORE_TYPE = "JKS";
    public static final String KEY_STORE_TYPE = "PKCS12";

    private String keyStoreLocation;
    private String trustStoreLocation;
    private TrustManagerFactory trustManagerFactory;
    private KeyManagerFactory keyManagerFactory;
    private KeyStore keyStore;
    private KeyStore trustStore;


    public void prepareUserCert( final char[] keyStorePassword, final char[] keyPassword )
            throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException,
            UnrecoverableKeyException
    {
        InputStream kis = new FileInputStream( keyStoreLocation );

        keyStore = KeyStore.getInstance( KEY_STORE_TYPE );
        keyStore.load( kis, keyStorePassword );
        keyManagerFactory = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
        keyManagerFactory.init( keyStore, keyPassword );
    }

    public void prepareTrustStore( final char[] trustStorePassword )
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException
    {
        InputStream tis = new FileInputStream( trustStoreLocation );

        trustStore = KeyStore.getInstance( TRUST_STORE_TYPE );
        trustStore.load( tis, trustStorePassword );
        trustManagerFactory = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm() );
        trustManagerFactory.init( trustStore );
    }

    public void setupSSLContext() throws KeyManagementException, NoSuchAlgorithmException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init( keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLContext.setDefault(sslContext);
    }


    public String getKeystoreLocation() {

        return keyStoreLocation;
    }


    public void setKeystoreLocation( final String keyStoreLocation ) {

        this.keyStoreLocation = keyStoreLocation;
    }


    public void setTrustStoreLocation( final String trustStoreLocation ) {

        this.trustStoreLocation = trustStoreLocation;
    }


    public KeyStore getKeyStore() {

        return keyStore;
    }

    public KeyStore getTrustStore() {

        return trustStore;
    }
}
