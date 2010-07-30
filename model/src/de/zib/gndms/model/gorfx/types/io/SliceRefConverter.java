package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
