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

package de.zib.gndms.taskflows.esgfStaging.client.tools;

import de.zib.gndms.common.model.gorfx.types.io.SliceOrderPropertyReader;
import de.zib.gndms.stuff.propertytree.PropertyTree;
import de.zib.gndms.stuff.propertytree.PropertyTreeFactory;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;

import java.util.Properties;

/**
 * @date: 12.03.12
 * @time: 15:02
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class ESGFStagingOrderPropertyReader extends SliceOrderPropertyReader< ESGFStagingOrder > {

    protected ESGFStagingOrderPropertyReader( ) {
        super( ESGFStagingOrder.class );
    }


    protected ESGFStagingOrderPropertyReader( Properties properties ) {
        super( ESGFStagingOrder.class, properties );
    }


    @Override
    public void read( ) {

        super.read( );
        
        Properties properties = getProperties();
        PropertyTree propertyTree = PropertyTreeFactory.createPropertyTree( properties );
        ESGFStagingOrder esgfStagingOrder = getProduct();
        
        PropertyTree subTree;
        for( int i = 1; ( subTree = propertyTree.subTree( Integer.toString( i ) ) ) != null; ++i ) {
            Properties p = subTree.asProperties( true );

            String url = ( String )p.get( ESGFStagingOrderProperties.ESGF_STAGING_URL.key );
            String checksum = ( String )p.get( ESGFStagingOrderProperties.ESGF_STAGING_CHECKSUM.key );
            if( null == url || null == checksum )
                continue;

            esgfStagingOrder.addLink( url, checksum );
        }
    }


    @Override
    public void done() {
    }
}
