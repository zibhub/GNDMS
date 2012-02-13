package de.zib.gndms.taskflows.dmsstaging.client.tools;

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



import de.zib.gndms.common.model.gorfx.types.io.OrderPropertyWriter;
import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.taskflows.staging.client.model.DataDescriptor;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorConverter;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorPropertyWriter;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorWriter;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 09.10.2008
 * Time: 18:16:50
 * To change this template use File | Settings | File Templates.
 */
public class SliceStageInOrderPropertyWriter extends OrderPropertyWriter implements SliceStageInOrderWriter {

    public SliceStageInOrderPropertyWriter(){
    }


    public SliceStageInOrderPropertyWriter( Properties properties ){
        super( properties );    
    }

    public void writeGridSiteName ( String gsn ){
        getProperties().setProperty( SfrProperty.GRID_SITE.key,gsn) ;
    }

    public void writeDataFileName( String dfn ){
        getProperties( ).setProperty( SfrProperty.BASE_FILE.key, dfn );
    }

    public void writeMetaDataFileName( String mfn ){
        getProperties( ).setProperty( SfrProperty.META_FILE.key, mfn );
    }

    public void writeDataDescriptor( DataDescriptor dps ) {
        DataDescriptorPropertyWriter dpw = new DataDescriptorPropertyWriter( getProperties() );
        DataDescriptorConverter conv = new DataDescriptorConverter( dpw, dps );
        conv.convert( );
    }

    public DataDescriptorWriter getDataDescriptorWriter( ) {
        return new DataDescriptorPropertyWriter( getProperties() );
    }

    public void beginWritingDataDescriptor( ){
        
    }

    public void doneWritingDataDescriptor( ){
        
    }

    public void done() {
        
    }
}
