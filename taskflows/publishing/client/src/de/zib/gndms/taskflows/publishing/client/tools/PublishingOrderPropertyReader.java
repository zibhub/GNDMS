/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.publishing.client.tools;

import de.zib.gndms.common.model.gorfx.types.io.OrderPropertyReader;
import de.zib.gndms.taskflows.publishing.client.model.PublishingOrder;

import java.util.Properties;

/**
 * @date: 12.03.12
 * @time: 15:02
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class    PublishingOrderPropertyReader extends OrderPropertyReader< PublishingOrder > {

    protected PublishingOrderPropertyReader( ) {
        super( PublishingOrder.class );
    }


    protected PublishingOrderPropertyReader( Properties properties ) {
        super( PublishingOrder.class, properties );
    }


    @Override
    public void read( ) {
        
        super.read( );
        
        Properties properties = getProperties();
        PublishingOrder publishingOrder = getProduct();
        
        final String sliceId;
        final String metafile;

        if( properties.containsKey( PublishingOrderProperties.PUBLISHING_SLICE.key ) )
            sliceId = properties.getProperty( PublishingOrderProperties.PUBLISHING_SLICE.key );
        else
            throw new IllegalArgumentException( "No Slice given in Publishing Order" );
        
        if( properties.containsKey( PublishingOrderProperties.PUBLISHING_METAFILE.key ) )
            metafile = properties.getProperty( PublishingOrderProperties.PUBLISHING_METAFILE.key );
        else
            throw new IllegalArgumentException( "No metadata filename in Publishing Order" );

        publishingOrder.setSliceId( sliceId );
        publishingOrder.setMetadataFile( metafile );
    }


    @Override
    public void done() {
    }
}
