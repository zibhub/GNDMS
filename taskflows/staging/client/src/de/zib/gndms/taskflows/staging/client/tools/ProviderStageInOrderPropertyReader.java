package de.zib.gndms.taskflows.staging.client.tools;

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


import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.common.model.gorfx.types.io.SliceOrderPropertyReader;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 15:21:09
 */
public class ProviderStageInOrderPropertyReader extends SliceOrderPropertyReader<ProviderStageInOrder> {

    public ProviderStageInOrderPropertyReader() {
        super( ProviderStageInOrder.class );
    }


    public ProviderStageInOrderPropertyReader( Properties properties ) {
        super( ProviderStageInOrder.class, properties );
    }


    public void read() {

        super.read();

        DataDescriptorPropertyReader dr = new DataDescriptorPropertyReader( getProperties() );
        dr.begin( );
        dr.read( );
        getProduct().setDataDescriptor( dr.getProduct() );

        if( getProperties( ).containsKey( SfrProperty.BASE_FILE.key ) )
            getProduct( ).setActDataFile( getProperties().getProperty( SfrProperty.BASE_FILE.key ) );
        
        getProduct( ).setActMetadataFile( getMandatoryProperty( SfrProperty.META_FILE.key ) );
    }


    public void done() {
        // not required here
    }


    public static ProviderStageInOrder readFromFile( final String fileName ) throws IOException {

        InputStream is = null;
        try {
            is = new FileInputStream( fileName );
            Properties prop = new Properties( );
            prop.load( is );
            is.close( );
            ProviderStageInOrderPropertyReader reader = new ProviderStageInOrderPropertyReader( prop );
            reader.performReading( );
            return  reader.getProduct();
        } finally {
            if( is != null )
                is.close( );
        }
    }
}
