package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 31.10.2008, Time: 17:23:52
 */
public abstract class CommonSliceResult extends AbstractTaskResult {

    public SliceRef sliceRef;

    
    protected CommonSliceResult( String offerType ) {
        super( offerType );
    }


    public SliceRef getSliceRef() {
        return sliceRef;
    }


    public void setSliceRef( SliceRef sliceRef ) {
        this.sliceRef = sliceRef;
    }
}
