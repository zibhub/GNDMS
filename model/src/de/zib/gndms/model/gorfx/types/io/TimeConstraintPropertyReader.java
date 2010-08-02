package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.model.gorfx.types.TimeConstraint;
import org.joda.time.DateTime;

import java.util.Properties;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
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

        if( min == null && max == null ) {
            deleteProduct( );
        } else {
            getProduct( ).setMinTime( min );
            getProduct( ).setMaxTime( max );
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
