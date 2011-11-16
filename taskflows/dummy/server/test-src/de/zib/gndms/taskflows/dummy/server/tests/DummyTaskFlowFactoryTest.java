/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.taskflows.dummy.server.tests;

import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.dummy.client.DummyTaskFlowMeta;
import de.zib.gndms.taskflows.dummy.client.model.DummyOrder;
import de.zib.gndms.taskflows.dummy.server.DummyTaskFlowFactory;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 09.11.11  17:52
 * @brief This is no dummy test, it is the test of the abstract DefaultTaskFlowFactory.
 *
 * Especially the cacheDao features are tested
 */
public class DummyTaskFlowFactoryTest {

    private DummyTaskFlowFactory factory = new DummyTaskFlowFactory();

    @Test( groups = { "factory", "taskFlows" } )
    public void taskFlowKeyTest( ) {
        String key = factory.getTaskFlowKey();
        Assert.assertEquals ( key, DummyTaskFlowMeta.TASK_FLOW_KEY );
    }


    @Test( groups = { "factory", "taskFlows" } )
    public void getCalculatorTest( ) {

        AbstractQuoteCalculator<?> calc  = factory.getQuoteCalculator();
        Assert.assertNotNull( calc );
    }


    @Test( groups = { "factory", "taskFlows" } )
    public void createAndFindTest( ) {

        TaskFlow<?> tf = factory.create();
        Assert.assertNotNull( tf, "create not null" );
        Assert.assertNotNull( tf.getId(), "id not null" );
        TaskFlow<?> tf2 = factory.find( tf.getId() );
        Assert.assertNotNull( tf2, "find not null" );
        Assert.assertSame( tf, tf2 );
    }


    @Test( dependsOnMethods = {"createAndFindTest"}, groups = { "factory", "taskFlows" } )
    public void findMissTest( ) {

        TaskFlow<?> tf = factory.find( "no-valid-id" );
        Assert.assertNull( tf );
    }


    @Test( dependsOnMethods = {"findMissTest"}, groups = { "factory", "taskFlows" } )
    public void createAndDeleteTest( ) {

        TaskFlow<?> tf = factory.create();
        Assert.assertNotNull( tf );
        TaskFlow<?> tf2 = factory.find( tf.getId() );
        Assert.assertSame( tf, tf2 );
        factory.delete( tf.getId() );
        TaskFlow<?> tf3 = factory.find( tf.getId() );
        Assert.assertNull( tf3 );
    }

    /*
    @Test( groups = { "factory", "taskFlows" } )
    public void maxCacheSizeBehaviour( ) throws InterruptedException {

        DummyTaskFlowFactory localFactory = new DummyTaskFlowFactory();
        int limit = localFactory.MAX_CACHE_SIZE + 1;
        Queue<String> keys = new ArrayDeque<String>( limit );
        TaskFlow<?> tf;
        // insert 1 item to much
        for ( int i=0; i < limit; ++i ) {
            tf = localFactory.create();
            keys.add( tf.getId() );
        }
        Thread.sleep(  1000 ); // give the cache time to reorder
        // lockup first item:
        tf = localFactory.find( keys.remove() );
        Assert.assertNull( tf, "first entry must have been removed from the cache." );
        // lockup second item:
        tf = localFactory.find( keys.remove() );
        Assert.assertNotNull( tf, "second key must be available" );
    }
    */

}
