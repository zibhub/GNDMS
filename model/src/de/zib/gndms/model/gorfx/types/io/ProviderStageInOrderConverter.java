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



import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:02:26
 */
public class ProviderStageInOrderConverter extends OrderConverter<ProviderStageInOrderWriter, ProviderStageInOrder> {


    public ProviderStageInOrderConverter() {
    }


    public ProviderStageInOrderConverter( ProviderStageInOrderWriter writer, ProviderStageInOrder providerStageInOrder ) {
        super( writer, providerStageInOrder );
    }


    public void convert( ) {

        super.convert( );
        DataDescriptorWriter ddw = getWriter( ).getDataDescriptorWriter();
        getWriter( ).beginWritingDataDescriptor();
        DataDescriptorConverter ddc = new DataDescriptorConverter( ddw,  getModel().getDataDescriptor() );
        ddc.convert( );
        getWriter( ).doneWritingDataDescriptor();
        if( getModel().hasDataFile() )
            getWriter().writeDataFileName( getModel().getActDataFile() );
        getWriter().writeMetaDataFileName( getModel().getActMetadataFile() );
        getWriter().done ();
    }
}
