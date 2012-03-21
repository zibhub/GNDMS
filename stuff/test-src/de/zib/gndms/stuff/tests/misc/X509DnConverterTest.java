package de.zib.gndms.stuff.tests.misc;
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

import de.zib.gndms.stuff.misc.X509DnConverter;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.security.auth.x500.X500Principal;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 21.03.12  18:03
 * @brief
 */
public class X509DnConverterTest {

    @Test( groups={ "stuff" } )
    public void testIt (  ) {
        
        final String openSslDn = "/C=country/O=Organisation/OU=Organisation Unit/CN=Common.name";
        final String rfc2253dn = X509DnConverter.toRfc2253Dn( openSslDn );

        System.out.println( "openssl dn: " + openSslDn );
        System.out.println( "RFC2253 dn: " + rfc2253dn );
        
        // Back to openssl
        final String backOpenSslDn = X509DnConverter.toOpenSslDn( rfc2253dn );
        Assert.assertEquals( openSslDn, backOpenSslDn, "toOpenSslDn matches original" );
        
        
        final  X500Principal principal = new X500Principal( rfc2253dn );
        
        Assert.assertEquals( principal.getName(), rfc2253dn, "rfc2253 export match" );
    }
}
