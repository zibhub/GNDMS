package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 13:38:15
 */
public class SliceRefConverter extends GORFXConverterBase<SliceRefWriter, SliceRef> {

    
    public SliceRefConverter() {
    }


    public SliceRefConverter( SliceRefWriter writer, SliceRef model ) {
        super( writer, model );
    }


    public void convert() {

      if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeSliceRef( getModel() );
        getWriter().writeSliceId( getModel().getResourceKeyValue() );
        getWriter().writeSliceGridSite( getModel().getGridSiteId() );
        getWriter().writeSliceResourceName( getModel( ).getResourceKeyName() );
        getWriter().done();
    }
}
