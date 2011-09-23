package de.zib.gndms.taskflows.interslicetransfer.client.tools;

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



import de.zib.gndms.model.gorfx.types.InterSliceTransferOrder;
import de.zib.gndms.model.gorfx.types.io.OrderConverter;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:48:27
 */
public class InterSliceTransferORQConverter extends OrderConverter<InterSliceTransferORQWriter, InterSliceTransferOrder> {

    public InterSliceTransferORQConverter() {
    }


    public InterSliceTransferORQConverter( InterSliceTransferORQWriter writer, InterSliceTransferOrder model ) {
        super( writer, model );
    }


    public void convert() {
        super.convert();
        getWriter( ).writeSourceSlice( getModel().getSourceSlice() );
        getWriter( ).writeDestinationSlice( getModel().getDestinationSlice() );
        if( getModel( ).hasFileMap( ) )
            getWriter( ).writeFileMap( getModel().getFileMap() );

        getWriter().done( );
    }
}
