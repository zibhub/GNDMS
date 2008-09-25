package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.*;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 13:28:36
 */
public class LevelRangePropertyReader extends AbstractPropertyReader<LevelRange> {

    public LevelRangePropertyReader( Class productClass ) {
        super( productClass );
    }


    public LevelRangePropertyReader( Properties properties ) {
        super( LevelRange.class, properties );
    }


    public void read() {

        Altitude mina = null;
        String na = getProperties( ).getProperty( SfrProperty.ALT_MINNAME.key );
        if( na != null )
            mina = new NamedAltitude( na );
        else
            mina = readNumericAltitude( SfrProperty.ALT_MIN.key, SfrProperty.ALT_UNIT_MIN.key );

        Altitude maxa = null;
        na = getProperties( ).getProperty( SfrProperty.ALT_MAXNAME.key );
        if( na != null )
            maxa = new NamedAltitude( na );
        else
            maxa = readNumericAltitude( SfrProperty.ALT_MAX.key, SfrProperty.ALT_UNIT_MAX.key );

        if( ! equivalent( mina, maxa ) )
            throw new IllegalStateException( "Malformed property file: LevelRange requires min and max altitude!" );

        getProduct().setMin( mina );
        getProduct().setMax( maxa );

        getProduct().setVerticalCRS( getProperties().getProperty( SfrProperty.ALT_VCRS.key ));
    }


    public void done() {
        // Not required here
    }
    

    public static LevelRange readLevelRange( Properties properties ) {
        LevelRangePropertyReader reader = new LevelRangePropertyReader( properties );
        reader.begin();
        reader.read();
        reader.done();

        return reader.getProduct();
    }


    private NumericAltitude readNumericAltitude( String vkey, String ukey ) {

        String dv = getProperties().getProperty( vkey );
        String u = getProperties().getProperty( ukey );

        if( ! equivalent( dv, u ) )
            throw new IllegalStateException( "Malformed property file: numeric altitude requires both " + ukey + " and " + ukey );

        if( vkey == null && ukey == null )
            return null;

        return new NumericAltitude( Double.parseDouble( dv ), AltitudeUnit.unitForString( u ) );
    }

    private boolean equivalent( Object a, Object b ) {
        return ( a == null && b == null ) || ( a != null && b != null );
    }
}
