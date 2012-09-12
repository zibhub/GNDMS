/*
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

package de.zib.gndms.common.model.gorfx.types.io;

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.model.gorfx.types.SliceOrder;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Properties;

/**
 * @date: 28.08.12
 * @time: 10:19
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class SliceOrderPropertyReader< M extends SliceOrder > extends OrderPropertyReader< M > {

    protected SliceOrderPropertyReader( Class productClass ) {
        super( productClass );
    }


    protected SliceOrderPropertyReader( Class productClass, Properties properties ) {
        super( productClass, properties );
    }


    @Override
    public void read( ) {

        super.read( );

        Properties properties = getProperties();
        SliceOrder sliceOrder = getProduct();
        
        final String sliceId = properties.getProperty( SliceOrderProperties.SLICE_ID.key );
        final String sliceSize = properties.getProperty( SliceOrderProperties.SLICE_SIZE.key );
        final String sliceTermTime = properties.getProperty( SliceOrderProperties.SLICE_TERMINATION_TIME.key );
        
        if( sliceId != null )
            sliceOrder.setSliceId( sliceId );
        else if( sliceSize != null || sliceTermTime != null ) {
            Long _sliceSize = null;
            DateTime _sliceTermTime = null;
            if( sliceSize != null )
                _sliceSize = Long.parseLong( sliceSize );
            if( sliceTermTime != null )
                _sliceTermTime = ISODateTimeFormat.dateTimeParser().parseDateTime( sliceTermTime );
            
            sliceOrder.setSliceConfiguration( new SliceConfiguration( _sliceSize, _sliceTermTime ) );
        }
    }
}
