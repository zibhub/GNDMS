package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.LevelRange;
import de.zib.gndms.model.gorfx.types.Altitude;
import de.zib.gndms.model.gorfx.types.NumericAltitude;
import de.zib.gndms.model.gorfx.types.NamedAltitude;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 18:16:04
 */
public class LevelRangeConverter extends GORFXConverterBase<LevelRangeWriter, LevelRange> {

    public LevelRangeConverter() {
    }


    public LevelRangeConverter( LevelRangeWriter writer, LevelRange model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel( ) == null )
            throw new IllegalStateException( );

        getWriter( ).begin();

        Altitude alt = getModel( ).getMin();
        if( alt instanceof NumericAltitude )
            getWriter( ).writeMinNumericAltitude( (NumericAltitude) alt );
        else if( alt instanceof NamedAltitude )
            getWriter( ).writeMinNamedAltitude( ( NamedAltitude ) alt );

        alt = getModel().getMax();
        if( alt instanceof NumericAltitude )
            getWriter( ).writeMaxNumericAltitude( (NumericAltitude) alt );
        else if( alt instanceof NamedAltitude )
            getWriter( ).writeMaxNamedAltitude( ( NamedAltitude ) alt );

        if( getModel( ).hasVerticalCRS() )
            getWriter( ).writeVericalCRS( getModel().getVerticalCRS() );

        getWriter( ).done();
    }
}
