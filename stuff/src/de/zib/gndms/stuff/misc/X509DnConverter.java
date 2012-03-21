package de.zib.gndms.stuff.misc;

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
}