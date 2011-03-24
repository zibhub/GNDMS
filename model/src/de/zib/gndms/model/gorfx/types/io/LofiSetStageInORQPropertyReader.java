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



import de.zib.gndms.model.gorfx.types.LofiSetStageInORQ;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:38:55
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQPropertyReader extends ORQPropertyReader<LofiSetStageInORQ>{

    public LofiSetStageInORQPropertyReader(){
       super( LofiSetStageInORQ.class ); 
    }
    protected LofiSetStageInORQPropertyReader( Properties properties) {
        super(LofiSetStageInORQ.class, properties);
    }

    public void read() {

        super.read();

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setActDataDescriptor( dr.getProduct() );

        if(! getProperties().containsKey( SfrProperty.GRID_SITE.key ) )
          getProduct().setActGridSite( getProperties().getProperty( SfrProperty.GRID_SITE.key ) );
        getProduct( ).setActDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        getProduct( ).setActMetadataFile( getProperties().getProperty( SfrProperty.META_FILE.key ) );

    }


    public void done() {
        
    }
}
