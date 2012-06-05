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

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 24.05.12  13:47
 * @brief
 */
public class PemReaderTest {



    public static void main( String[] args ) throws Exception {

        if ( args.length != 2 ) {
            System.err.println( "usage: PemReaderTest <pem-location> <pk-pass-phase>" );
            System.exit( 1 );
        }

        Security.addProvider( new org.bouncycastle.jce.provider.BouncyCastleProvider() );
        CertStuffHolder certStuff = readKeyPair( new File( args[0] ), args[1].toCharArray() );

        System.out.println( certStuff.getKeyPair() );
        // System.out.println( keyPair.getPrivate() );

        System.exit( 0 );

    }

    public static CertStuffHolder readKeyPair( File privateKey,
                                               char[] keyPassword ) throws IOException {

        FileReader fileReader = new FileReader(privateKey);
        PEMReader pemReader = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
        try {
            Object obj;
            ArrayList<X509Certificate> chain = new ArrayList<X509Certificate>(1);
            KeyPair keyPair = null;

            CertStuffHolder csh = new CertStuffHolder();


            while ((obj = pemReader.readObject()) != null) {
                if (obj instanceof X509Certificate ) {
                    X509Certificate cert = ( X509Certificate ) obj;
                    System.out.println( "read cert: " + cert.toString() );
                    chain.add( cert );
                }
                if (obj instanceof KeyPair ) {
                    keyPair = ( KeyPair ) obj;
                    System.out.println( "read keypair: " + keyPair.toString() );
                    System.out.println( "read keypair: " + keyPair.getPrivate() );
                    System.out.println( "read keypair: " + keyPair.getPublic() );
                }
            }

            csh.setChain( chain );
            csh.setKeyPair( keyPair );

            return csh;
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            pemReader.close();
            fileReader.close();
        }
    }

    private static class DefaultPasswordFinder implements PasswordFinder {

        private final char [] password;

        private DefaultPasswordFinder(char [] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return Arrays.copyOf( password, password.length );
        }
    }

}
