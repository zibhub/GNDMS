package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.Altitude;
import de.zib.gndms.model.gorfx.types.NumericAltitude;
import de.zib.gndms.model.gorfx.types.NamedAltitude;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:46:27
 */
public interface LevelRangeWriter extends GORFXWriterBase {


    public void writeMaxNumericAltitude( NumericAltitude max );
    public void writeMinNumericAltitude( NumericAltitude min );

    public void writeMaxNamedAltitude( NamedAltitude max );
    public void writeMinNamedAltitude( NamedAltitude min );
    
    public void writeVericalCRS( String vcrs );
}
