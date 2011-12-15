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



import de.zib.gndms.common.model.gorfx.types.io.OrderPropertyReader;
import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.taskflows.dmsstaging.client.model.SliceStageInOrder;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorPropertyReader;

import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 09.10.2008
 * Time: 18:16:38
 * To change this template use File | Settings | File Templates.
 */
public class SliceStageInOrderPropertyReader extends OrderPropertyReader<SliceStageInOrder> {

    public SliceStageInOrderPropertyReader() {
        super( SliceStageInOrder.class );
    }

    public SliceStageInOrderPropertyReader( Properties properties ) {
        super( SliceStageInOrder.class, properties );
    }

    public void read() {

        super.read();

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setDataDescriptor( dr.getProduct() );

        if(! getProperties().containsKey( SfrProperty.GRID_SITE.key ) )
          getProduct().setGridSite( getProperties().getProperty( SfrProperty.GRID_SITE.key ) );
        getProduct( ).setActDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        getProduct( ).setActMetadataFile( getProperties().getProperty( SfrProperty.META_FILE.key ) );

    }

    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
