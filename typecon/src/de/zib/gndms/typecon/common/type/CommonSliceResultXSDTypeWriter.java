package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.io.CommonSliceResultWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefWriter;
import de.zib.gndms.model.gorfx.types.CommonSliceResult;
import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 12:45:15
 */
public abstract class CommonSliceResultXSDTypeWriter extends AbstractXSDTypeWriter<CommonSliceResult>
    implements CommonSliceResultWriter {

    private SliceRefXSDTypeWriter sliceWriter;


    // use the doneWritingSliceRef-method to obtain the result from the
    // sliceWriter and add it to your result.


    public void beginWritingSliceRef() {
        if( sliceWriter == null )
            throw new IllegalStateException( "SliceRefXSDTypeWriter is not present" );
    }


    public SliceRefWriter getSliceRefWriter() {
        if( sliceWriter == null )
            sliceWriter = new SliceRefXSDTypeWriter( );
        return sliceWriter;
    }


    public void begin() {
        // not required here
    }


    public void done() {
        // not required here
    }
}
