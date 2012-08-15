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

package de.zib.gndms.gndmc.gorfx.Test;

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.GORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.taskflows.failure.client.FailureTaskFlowMeta;
import de.zib.gndms.taskflows.failure.client.model.FailureOrder;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @date: 25.04.12
 * @time: 13:17
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class TaskFlowClientTest {
    final static private String ORDER_MESSAGE = "TASKFLOWTEST MESSAGE";
    final static private String TASKFLOW_WID = "GORFXTaskFlowTEST";
    
    final ApplicationContext context;

    private GORFXClient gorfxClient;
    private TaskFlowClient taskFlowClient;

    final private String serviceUrl;
    final private String admindn;
    
    private String taskFlowId;
    private Specifier< Facets > taskSpecifier;


    @Parameters( { "serviceUrl", "admindn" } )
    public TaskFlowClientTest( final String serviceUrl, @Optional( "root" ) final String admindn ) {
        this.serviceUrl = serviceUrl;
        this.admindn = admindn;

        this.context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
    }


    @BeforeClass( groups = { "GORFXServiceTest" } )
    public void init() {
        gorfxClient = ( GORFXClient )context.getAutowireCapableBeanFactory().createBean(
                GORFXClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        gorfxClient.setServiceURL( serviceUrl );

        taskFlowClient = ( TaskFlowClient )context.getAutowireCapableBeanFactory().createBean(
                TaskFlowClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        taskFlowClient.setServiceURL( serviceUrl );
    }


    @Test( groups = { "TaskFlowClientTest" } )
    public void createTaskFlow() {
        FailureOrder order = new FailureOrder();
        order.setMessage( "TESTING TaskFlow creation" );
        order.setWhere( FailureOrder.FailurePlace.NOWHERE );

        ResponseEntity<Specifier< Facets > > responseEntity = gorfxClient.createTaskFlow(
                FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                order,
                admindn,
                TASKFLOW_WID,
                new LinkedMultiValueMap< String, String >()
        );

        Assert.assertNotNull( responseEntity );
        Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.CREATED );
        
        taskFlowId = responseEntity.getBody().getUriMap().get( UriFactory.TASKFLOW_ID );
    }


    @Test( groups = { "TaskFlowClientTest" }, dependsOnMethods = { "createTaskFlow" } )
    public void listFacets() {
        ResponseEntity< Facets > taskFlowFacets;
        taskFlowFacets = taskFlowClient.getFacets( FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY, taskFlowId, admindn );

        Assert.assertNotNull( taskFlowFacets );
        Assert.assertEquals( taskFlowFacets.getStatusCode(), HttpStatus.OK );
    }


    @Test( groups = { "TaskFlowClientTest" }, dependsOnMethods = { "createTaskFlow" } )
    public void changeOrder() {
        // set new Order
        {
            FailureOrder order = new FailureOrder();
            order.setMessage( ORDER_MESSAGE );
            order.setWhere( FailureOrder.FailurePlace.NOWHERE );

            final ResponseEntity< Void > responseEntity = taskFlowClient.setOrder(
                    FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                    taskFlowId,
                    order,
                    admindn,
                    TASKFLOW_WID );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        }
        
        // check new order
        {
            final ResponseEntity< Order > responseEntity = taskFlowClient.getOrder(
                    FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                    taskFlowId,
                    admindn,
                    TASKFLOW_WID );
            
            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
            Assert.assertEquals( responseEntity.getBody() instanceof FailureOrder, true );
            
            FailureOrder order = ( FailureOrder )responseEntity.getBody();
            Assert.assertEquals( order.getMessage(), ORDER_MESSAGE );
        }
    }

    /**
     * create the task flow by creating a task without talking about quotes
     *
     * TODO: test quotes
     */
    @Test( groups = { "TaskFlowClientTest" }, dependsOnMethods = { "listFacets", "changeOrder" } )
    public void createTask() {
        final ResponseEntity< Specifier< Facets > > responseEntity = taskFlowClient.createTask(
                FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                taskFlowId,
                null,
                admindn,
                TASKFLOW_WID );

        Assert.assertNotNull( responseEntity );
        Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.CREATED );
        
        taskSpecifier = responseEntity.getBody();
    }


    @Test( groups = { "TaskFlowClientTest" }, dependsOnMethods = { "createTask" } )
    public void deleteTaskFlow() throws InterruptedException {
        Thread.sleep( 5000 );
        
        // delete task flow
        {
            final ResponseEntity< Integer > responseEntity = taskFlowClient.deleteTaskflow(
                    FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                    taskFlowId,
                    admindn,
                    TASKFLOW_WID );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        }

        // try to not find taskflow
        {
            try {
                final ResponseEntity<Facets> responseEntity= taskFlowClient.getFacets(
                        FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                        taskFlowId,
                        admindn );
                
                Assert.assertNotNull( responseEntity );
                Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.NOT_FOUND );
            }
            catch( HttpClientErrorException e ) {
                Assert.assertEquals( e.getStatusCode(), HttpStatus.NOT_FOUND );
            }
        }
    }
}
