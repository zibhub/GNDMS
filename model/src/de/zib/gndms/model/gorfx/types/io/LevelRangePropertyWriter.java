package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.NumericAltitude;
import de.zib.gndms.model.gorfx.types.NamedAltitude;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:54:31
 */
public class LevelRangePropertyWriter extends AbstractPropertyIO implements LevelRangeWriter {

    public LevelRangePropertyWriter() {
    }


    public LevelRangePropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeMaxNumericAltitude( NumericAltitude max ) {
        writeNumericAltitude( SfrProperty.ALT_MAX.key, SfrProperty.ALT_UNIT_MAX.key, max );
    }


    public void writeMinNumericAltitude( NumericAltitude min ) {
        writeNumericAltitude( SfrProperty.ALT_MIN.key, SfrProperty.ALT_UNIT_MIN.key, min );

    }


    public void writeMaxNamedAltitude( NamedAltitude max ) {
        writeNamedAltitude( SfrProperty.ALT_MAXNAME.key, max );
    }


    public void writeMinNamedAltitude( NamedAltitude min ) {
        writeNamedAltitude( SfrProperty.ALT_MINNAME.key, min );
    }


    public void writeVericalCRS( String vcrs ) {
        getProperties().setProperty( SfrProperty.ALT_VCRS.key, vcrs );
    }


    public void done() {
        // nothing to do
    }


    private void writeNumericAltitude( String vkey, String ukey, NumericAltitude alt ) {
        getProperties().setProperty( vkey, alt.getValue().toString( ) );
        getProperties().setProperty( ukey, alt.getUnit().getAbbreviation() );
    }

    
    private void writeNamedAltitude( String key, NamedAltitude alt ) {
        getProperties().setProperty( key, alt.getName() );
    }
}
