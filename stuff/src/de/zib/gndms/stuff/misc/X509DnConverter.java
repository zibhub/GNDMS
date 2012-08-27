package de.zib.gndms.stuff.misc;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class X509DnConverter {

    public static String toOpenSslDn( final String dn ) {

        final String newSep = "/";
        final String oldSep = ",";
        return newSep + reorderDN( dn, oldSep, newSep );
    }


    public static String toRfc2253Dn( final String dn ) {

        final String oldSep = "/";
        final String newSep = ",";
        return reorderDN( dn, oldSep, newSep );
    }


    public static String reorderDN( final String openSSLDn, final String oldSep,
                                    final String newSep )
    {

        String[] dnParts = openSSLDn.split( oldSep );
        StringBuilder sb = new StringBuilder();
        int startIdx = 0;
        if ( openSSLDn.startsWith( oldSep ) )
            startIdx = 1;

        for ( int i = dnParts.length - 1; i > startIdx - 1; --i ) {
            sb.append( dnParts[i] );
            if ( i != startIdx )
                sb.append( newSep );
        }
        return sb.toString();
    }


    public static String openSslDnExtractCn( final String dn ) {

        final String sep = "/";
        return extractCn( dn, sep );
    }

    public static String rfc2253DnExtractCn( final String dn ) {

        final String sep = ",";
        return extractCn( dn, sep );
    }


    private static String extractCn( final String dn, final String sep ) {

        Pattern pattern = Pattern.compile( ".*CN=host/([^" +sep+ "]*).*", Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher( dn );

        if( matcher.matches() )
            return matcher.group( 1 );

        pattern = Pattern.compile( ".*CN=([^" +sep+ "]*).*", Pattern.CASE_INSENSITIVE );
        matcher = pattern.matcher( dn );

        if( matcher.matches() )
            return matcher.group( 1 );
        else
            throw new IllegalStateException( "No CN string in DN: " + dn );
    }
}