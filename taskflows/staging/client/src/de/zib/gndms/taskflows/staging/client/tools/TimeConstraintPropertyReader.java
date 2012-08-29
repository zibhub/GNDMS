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



import de.zib.gndms.common.model.gorfx.types.io.AbstractPropertyReader;
import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.taskflows.staging.client.model.TimeConstraint;

import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 14:25:59
 */
public class TimeConstraintPropertyReader extends AbstractPropertyReader<TimeConstraint> {

    public TimeConstraintPropertyReader( ) {
        super( TimeConstraint.class );
    }


    public TimeConstraintPropertyReader( Properties properties ) {
        super( TimeConstraint.class, properties );
    }


    public void read() {

        // DateTime min = PropertyReadWriteAux.readISODateTime( getProperties(), SfrProperty.TIME_MIN.key );
         String min = getProperties().getProperty( SfrProperty.TIME_MIN.key );

        // DateTime max = PropertyReadWriteAux.readISODateTime( getProperties(), SfrProperty.TIME_MAX.key );
        String max = getProperties().getProperty( SfrProperty.TIME_MAX.key );
        
        String aggregation = getProperties().getProperty( SfrProperty.TIME_AGGREGATION.key );

        if( min == null && max == null || aggregation == null ) {
            deleteProduct( );
        } else {
            getProduct( ).setMinTime( min );
            getProduct( ).setMaxTime( max );
            getProduct( ).setAggregation( aggregation );
        }
    }


    public static TimeConstraint readTimeConstraint( Properties prop ) {
        TimeConstraintPropertyReader tcr = new TimeConstraintPropertyReader( prop );
        tcr.begin( );
        tcr.read( );
        return tcr.getProduct( );
    }

    public void done() {

    }
}
