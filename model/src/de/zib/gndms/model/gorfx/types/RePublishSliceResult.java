package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:16:04
 */
public class RePublishSliceResult extends CommonSliceResult {

    public RePublishSliceResult( ) {
        super( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
    }


    public RePublishSliceResult( SliceRef destinationSlice ) {
        super( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
        setSliceRef( destinationSlice );
    }
}
