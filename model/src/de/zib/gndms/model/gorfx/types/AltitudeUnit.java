package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:26:31
 */
public enum AltitudeUnit {
    HECTO_PASCAL( "hPa" ),
    METER( "m" ),
    LEVEL( "level" );

    private final String Abbreviation;

    private AltitudeUnit( String abbr ) {
        Abbreviation = abbr;
    }


    public String getAbbreviation() {
        return Abbreviation;
    }


    public static AltitudeUnit unitForString( String s ) {

        AltitudeUnit au[] = AltitudeUnit.values();

        for ( int i=0; i < au.length; ++i )
            if ( s.equals( au[i].getAbbreviation() ) )
                return au[i];

        throw new IllegalArgumentException( "No unit for " + s );
    }
}
