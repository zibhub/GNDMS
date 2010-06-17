package de.zib.gndms.model.gorfx.types.io;

import java.util.HashMap;

/**
 * An GORFXWriter for ORQs.
 *
 * Provides the methods which are required to write an orq to a desired type.
 *
 * It should be used in conjunction with an ORQConverter.
 *
 * @see de.zib.gndms.model.gorfx.types.AbstractORQ
 * @see de.zib.gndms.model.gorfx.types.io.ORQConverter
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:02:56
 */
public interface ORQWriter extends GORFXWriterBase {

    public void writeJustEstimate( boolean je );

    public void writeContext( HashMap<String,String> ctx );

    public void writeId( String id );
}
