package de.zib.gndms.gndmc.dspace.test;

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

import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.logic.model.dspace.SubspaceConfiguration;
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

public class SubspaceClientTest {

    final ApplicationContext context;

    private SubspaceClient subspaceClient;

    final private String serviceUrl;

    final static String subspaceId = "testsub";
    final static String subspacePath = "/tmp/gndms/sub";
    final static String gridFtpPath = "gridFtpPath";
    final static boolean visible = true;
    final static long value = 6000;

    final static String sliceKindId = "testkind";
    final static String sliceKindConfig = "sliceKindMode:700; uniqueDirName:kind";

    final static String sliceConfig = "terminationTime:2011-12-16; sliceSize:1024";

    final private String admindn;


    @Parameters( { "serviceUrl", "admindn" } )
    public SubspaceClientTest( final String serviceUrl, @Optional("root") final String admindn ) {
        this.serviceUrl = serviceUrl;
        this.admindn = admindn;

        this.context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
    }


    @BeforeClass( groups = { "subspaceServiceTest" } )
    public void init() {
        subspaceClient = ( SubspaceClient )context.getAutowireCapableBeanFactory().createBean(
                SubspaceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        subspaceClient.setServiceURL( serviceUrl );
    }


    @Test( groups = { "subspaceServiceTest" } )
    public void testCreateSubspace() {
        final String mode = "CREATE";
        SubspaceConfiguration config = new SubspaceConfiguration( subspacePath, gridFtpPath, visible, value, mode, subspaceId );
        final ResponseEntity< Facets > subspace = subspaceClient.createSubspace( subspaceId, config, admindn );
        Assert.assertNotNull( subspace );
        Assert.assertEquals( subspace.getStatusCode(), HttpStatus.CREATED );

        final ResponseEntity< Facets > res = subspaceClient.listAvailableFacets( subspaceId, admindn );
        Assert.assertNotNull( res );
        Assert.assertEquals( res.getStatusCode(), HttpStatus.OK );
    }


    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testCreateSubspace" }
    )
    public void testCreateSliceKind() {
        final ResponseEntity< List< Specifier< Void > > > sliceKind =
                subspaceClient.createSliceKind( subspaceId, sliceKindId, sliceKindConfig, admindn );
        Assert.assertNotNull( sliceKind );
        Assert.assertEquals( sliceKind.getStatusCode(), HttpStatus.CREATED );

        final ResponseEntity< List< Specifier< Void > > > listResponseEntity
                = subspaceClient.listSliceKinds(subspaceId, admindn);
        final List< Specifier< Void > > specifierList = listResponseEntity.getBody();
        
        for( Specifier< Void > s: specifierList ) {
            if( ! s.getUriMap().containsKey( UriFactory.SLICE_KIND ) )
                continue;
            if( s.getUriMap().get( UriFactory.SLICE_KIND ).equals( sliceKindId ) )
                return;
        }

        throw new IllegalStateException( "The created SliceKind " + sliceKindId
                + " could not be found in SliceKindListing" );
    }

    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testCreateSliceKind" }
    )
    public void testDeleteSubspace() {
        final ResponseEntity< Specifier< Void > > responseEntity = subspaceClient.deleteSubspace( subspaceId, admindn );
        Assert.assertNotNull( responseEntity );
        Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );

        // wait for task to finish
    }
}
