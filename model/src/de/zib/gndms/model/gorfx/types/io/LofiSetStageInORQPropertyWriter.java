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



import de.zib.gndms.model.gorfx.types.DataDescriptor;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:39:03
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQPropertyWriter extends ORQPropertyWriter implements LofiSetStageInORQWriter {

    public LofiSetStageInORQPropertyWriter(){
        
    }

    public LofiSetStageInORQPropertyWriter( Properties properties){
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
