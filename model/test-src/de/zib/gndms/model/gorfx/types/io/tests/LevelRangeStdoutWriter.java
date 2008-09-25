package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.io.LevelRangeWriter;
import de.zib.gndms.model.gorfx.types.NumericAltitude;
import de.zib.gndms.model.gorfx.types.NamedAltitude;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 12:59:33
 */
public class LevelRangeStdoutWriter implements LevelRangeWriter {

    public void writeMaxNumericAltitude( NumericAltitude max ) {
        System.out.println( "   Max Altitude: "  );
        showNumericAltitude( max );
    }


    public void writeMinNumericAltitude( NumericAltitude min ) {
        System.out.println( "   Min Altitude: "  );
        showNumericAltitude( min );
    }


    public void writeMaxNamedAltitude( NamedAltitude max ) {
        System.out.println( "   Max Altitude: " + max.getName() );
    }


    public void writeMinNamedAltitude( NamedAltitude min ) {
        System.out.println( "   Min Altitude: " + min.getName() );
    }


    public void writeVericalCRS( String s ) {
        System.out.println( "   VerticalCRS: " + s );
    }


    public void begin() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void showNumericAltitude( NumericAltitude na ) {
        System.out.println( "    " + na.getValue().toString() + " " + na.getUnit().getAbbreviation() );
    }
}
