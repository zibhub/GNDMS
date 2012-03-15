package de.zib.gndms.gndmc.test;
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

import de.zib.gndms.gndmc.security.SetupSSL;

import java.io.Console;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 05.03.12  18:21
 * @brief
 */
public class KeyStoreTest {
    
    
    public static void main( String[] args ) throws Exception {

        Console con = System.console();
        //char[] kpass = con.readPassword();
        final char[] kpass = null; // "<fill-me>".toCharArray();
        final char[] kpass2 = kpass;
        final char[] truststorePassword = null; // "<fill-me>".toCharArray();

        if( kpass == null )
            throw new Exception( "fill in the passwords" );

        initSSL( kpass, kpass2, truststorePassword );

        System.exit( 0 );
    }


    public static SetupSSL initSSL( final char[] kpass, final char[] kpass2,
                                final char[] truststorePassword )
            throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException,
            UnrecoverableEntryException
    {

        SetupSSL sslSetup = new SetupSSL();
        sslSetup.setKeystoreLocation( "/tmp/awicert.p12" );
        sslSetup.prepareUserCert( kpass, kpass2 );

        KeyStore ks = sslSetup.getKeyStore();
        analyseKeyStore( kpass, ks );

        System.out.println( "now the trustStore" );


        sslSetup.setTrustStoreLocation( "/tmp/keystore" );
        sslSetup.prepareTrustStore( truststorePassword );

        analyseKeyStore( new char[1], sslSetup.getTrustStore() );

        return sslSetup;
    }


    private static void analyseKeyStore( final char[] kpass, final KeyStore ks )
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException
    {

        final Enumeration<String> aliases = ks.aliases();
        while ( aliases.hasMoreElements() ) {
            String ali = aliases.nextElement();

            if( ks.entryInstanceOf( ali, KeyStore.PrivateKeyEntry.class ) ) {
                System.out.println( ali + ": is private key" );
                KeyStore.PrivateKeyEntry ent = ( KeyStore.PrivateKeyEntry ) ks.getEntry( ali, new KeyStore.PasswordProtection( kpass ) );
                showDN( ( X509Certificate ) ent.getCertificate() );
            } else if ( ks.entryInstanceOf( ali, KeyStore.SecretKeyEntry.class ) )
                System.out.println( ali + ": is secret key" );
            else if ( ks.entryInstanceOf( ali, KeyStore.TrustedCertificateEntry.class ) ) {
                System.out.println( ali + ": is trusted cert" );
                showDN( ( ( KeyStore.TrustedCertificateEntry) ks.getEntry( ali, null ) ).getTrustedCertificate() );
            }
        }
    }


    private static void showDN( final Certificate certificate ) {

        final X509Certificate cert = ( X509Certificate ) certificate;
        System.out.println( cert.getSubjectDN() );
    }

}
