package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.gorfx.types.RePublishSliceOrder;

/**
 * Converter for publish slice orqs.
 *
 * Pretty much the same as the InterSliceTransfer converter, but doesn't write the destination slice,
 * cause it isn't part of the initial request.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:48:27
 */
public class RePublishSliceOrderConverter extends OrderConverter<RePublishSliceOrderWriter, RePublishSliceOrder> {

    public RePublishSliceOrderConverter() {
    }


    public RePublishSliceOrderConverter( RePublishSliceOrderWriter writer, RePublishSliceOrder model ) {
        super( writer, model );
    }


    public void convert() {
        super.convert();
        getWriter( ).writeSourceSlice( getModel().getSourceSlice() );
        if( getModel( ).hasFileMap( ) )
            getWriter( ).writeFileMap( getModel().getFileMap() );

        getWriter().done( );
    }
}
