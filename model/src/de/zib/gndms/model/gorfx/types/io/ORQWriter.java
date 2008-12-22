package de.zib.gndms.model.gorfx.types.io;

import java.util.HashMap;

/**
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
