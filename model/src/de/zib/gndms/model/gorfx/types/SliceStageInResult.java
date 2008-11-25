package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.11.2008, Time: 15:32:37
 */
public class SliceStageInResult extends CommonSliceResult {

    public SliceStageInResult( ) {
        super( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }

    public SliceStageInResult( SliceRef sr ) {
        super( GORFXConstantURIs.SLICE_STAGE_IN_URI );
        setSliceRef( sr );
    }
}
