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

package de.zib.gndms.taskflows.failure.client.tools;

import de.zib.gndms.common.model.gorfx.types.io.OrderPropertyReader;
import de.zib.gndms.stuff.propertytree.PropertyTree;
import de.zib.gndms.stuff.propertytree.PropertyTreeFactory;
import de.zib.gndms.taskflows.failure.client.model.FailureOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @date: 12.03.12
 * @time: 15:02
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class FailureOrderPropertyReader extends OrderPropertyReader< FailureOrder > {

    protected FailureOrderPropertyReader( ) {
        super( FailureOrder.class );
    }


    protected FailureOrderPropertyReader( Properties properties ) {
        super( FailureOrder.class, properties );
    }


    @Override
    public void read( ) {

        super.read( );
        
        Properties properties = getProperties();
        FailureOrder failureOrder = getProduct();

        final String where = properties.getProperty( FailureOrderProperties.FAILURE_WHERE.key );
        final String beforeSuper = properties.getProperty(FailureOrderProperties.FAILURE_BEFORE_SUPER.key );
        final String throwInSession = properties.getProperty(FailureOrderProperties.FAILURE_THROW_IN_SESSION.key );

        if( where != null )
            failureOrder.setWhere( FailureOrder.FailurePlace.valueOf( where ) );
        if( beforeSuper != null )
            failureOrder.setBeforeSuper( Boolean.getBoolean( beforeSuper ) );
        if( throwInSession != null )
            failureOrder.setThrowInSession( Boolean.getBoolean( throwInSession ) );

        final PropertyTree tree = PropertyTreeFactory.createPropertyTree( properties );
        final PropertyTree treeSleepBefore = tree.subTree( "sleepAfter" );
        final PropertyTree treeSleepAfter = tree.subTree( "sleepAfter" );

        final Properties propSleepBefore = treeSleepBefore.asProperties( true );
        final Map< FailureOrder.FailurePlace, Integer > mapSleepBefore = new HashMap< FailureOrder.FailurePlace, Integer >();
        for( String s: propSleepBefore.stringPropertyNames() ) {
            mapSleepBefore.put( FailureOrder.FailurePlace.valueOf( s ), Integer.valueOf( propSleepBefore.getProperty( s ) ) );
        }

        final Properties propSleepAfter = treeSleepAfter.asProperties( true );
        final Map< FailureOrder.FailurePlace, Integer > mapSleepAfter = new HashMap< FailureOrder.FailurePlace, Integer >();
        for( String s: propSleepAfter.stringPropertyNames() ) {
            mapSleepAfter.put( FailureOrder.FailurePlace.valueOf( s ), Integer.valueOf( propSleepAfter.getProperty( s ) ) );
        }

        failureOrder.setSleepBefore( mapSleepBefore );
        failureOrder.setSleepAfter( mapSleepAfter );
    }


    @Override
    public void done() {
    }
}
