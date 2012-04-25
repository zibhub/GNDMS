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

import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.gorfx.GORFXClient;
import de.zib.gndms.taskflows.failure.client.FailureTaskFlowMeta;
import de.zib.gndms.taskflows.failure.client.model.FailureOrder;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @date: 25.04.12
 * @time: 12:11
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class GORFXClientTest {
    final ApplicationContext context;

    private GORFXClient gorfxClient;

    final private String serviceUrl;
    final private String admindn;


    @Parameters( { "serviceUrl", "admindn" } )
    public GORFXClientTest( final String serviceUrl, @Optional( "root" ) final String admindn ) {
        this.serviceUrl = serviceUrl;
        this.admindn = admindn;

        this.context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
    }


    @BeforeClass( groups = { "GORFXServiceTest" } )
    public void init() {
        gorfxClient = ( GORFXClient )context.getAutowireCapableBeanFactory().createBean(
                GORFXClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        gorfxClient.setServiceURL(serviceUrl);

        //restTemplate = ( RestTemplate )context.getAutowireCapableBeanFactory().getBean( "restTemplate" );
    }


    @Test( groups = { "GORFXServiceTest" } )
    public void listFacets() {
        ResponseEntity< Facets > gorfxFacets;
        gorfxFacets = gorfxClient.listAvailableFacets( admindn );

        Assert.assertNotNull( gorfxFacets );
        Assert.assertEquals( gorfxFacets.getStatusCode(), HttpStatus.OK );
    }


    @Test( groups = { "GORFXServiceTest" } )
    public void listTaskFlows() {
        ResponseEntity< List< String > > responseEntity = gorfxClient.listTaskFlows( admindn );

        Assert.assertNotNull( responseEntity );
        Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
    }



    @Test( groups = { "GORFXServiceTest" }, dependsOnMethods = { "listFacets", "listTaskFlows" } )
    public void createTaskFlow() {
        // create TaskFlow
        {
            FailureOrder order = new FailureOrder();
            order.setMessage( "TESTING TaskFlow creation" );
            order.setWhere( FailureOrder.FailurePlace.NOWHERE );

            ResponseEntity<Specifier< Facets > > responseEntity = gorfxClient.createTaskFlow(
                    FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                    order,
                    admindn,
                    "GORFXTaskFlowTEST",
                    new LinkedMultiValueMap< String, String >()
            );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.CREATED );
        }

        // get TaskFlow Info
        //{
        //    ResponseEntity< TaskFlowInfo > responseEntity = gorfxClient.getTaskFlowInfo(
        //            FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY, admindn );
        //
        //    Assert.assertNotNull( responseEntity );
        //    Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        //}
    }
}
