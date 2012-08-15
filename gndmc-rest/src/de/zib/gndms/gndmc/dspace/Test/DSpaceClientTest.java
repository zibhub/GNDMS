package de.zib.gndms.gndmc.dspace.Test;

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

import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.dspace.DSpaceClient;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class DSpaceClientTest {
    final ApplicationContext context;

    private DSpaceClient dSpaceClient;
    final private String serviceUrl;

    final private String admindn;

    @Parameters( { "serviceUrl", "admindn" } )
    public DSpaceClientTest( final String serviceUrl, @Optional("root") final String admindn ) {
        this.serviceUrl = serviceUrl;
        this.admindn = admindn;

        this.context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
    }


    @BeforeClass( groups = { "dspaceServiceTest" } )
    public void init() {
        dSpaceClient = ( DSpaceClient )context.getAutowireCapableBeanFactory().createBean(
                DSpaceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        dSpaceClient.setServiceURL( serviceUrl );
    }


    @Test( groups = { "dspaceServiceTest" } )
    public void testListSubspaceSpecifiers() {
        final ResponseEntity< List< Specifier< Void > > > listResponseEntity
                = dSpaceClient.listSubspaceSpecifiers( admindn );
        
        Assert.assertNotNull( listResponseEntity );
        Assert.assertEquals( listResponseEntity.getStatusCode(), HttpStatus.OK );
    }
}
