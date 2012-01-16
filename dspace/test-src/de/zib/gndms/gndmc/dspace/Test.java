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

package de.zib.gndms.gndmc.dspace;

import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.dspace.service.SubspaceServiceImpl;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.NoWSDontNeedModelUpdateListener;
import de.zib.gndms.logic.model.dspace.*;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: explicit
 * Date: 02.12.11
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class Test extends JPATest
{
    String sliceId;

    @org.testng.annotations.Test( groups = { "jpa" } )
    public void test1() {
        EntityManager em = emf.createEntityManager();

        String subspace = "sub";
        TestTable t = new TestTable();

        t.setA( "blub A" );

        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
          //  em.persist(t);

            Subspace space = new Subspace();
            space.setAvailableSize(100);
            space.setGsiFtpPath("gridftppath");
            space.setPath("/tmp/gndms/sub");
            space.setId( subspace );
            em.persist( space );

            SubspaceConfiguration subspaceConfig = new SubspaceConfiguration( "/var/tmp/gndms/subpath", "gridftppath", true, 100, SetupMode.CREATE, "subrest" );
            SetupSubspaceAction action = new SetupSubspaceAction( subspaceConfig );

            StringWriter sw = new StringWriter();

            action.setClosingEntityManagerOnCleanup( false );
            action.setOwnEntityManager(em);
            action.setPrintWriter(new PrintWriter(sw));
            log.info("Calling action for setting up the supspace "
                    + subspace + ".");
            action.setOwnPostponedEntityActions(new DefaultBatchUpdateAction<GridResource>());
            action.getPostponedEntityActions().setListener( new NoWSDontNeedModelUpdateListener() );
            action.call();

            transaction.commit();
        }

        finally {
            try {
                em.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }


    @org.testng.annotations.Test( groups = { "jpa" } )
    public void test2() {
        EntityManager em = emf.createEntityManager();
        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Subspace sub = em.find( Subspace.class, "sub" );
            Assert.assertNotNull( sub );
            Assert.assertEquals( sub.getGsiFtpPath(), "gridftppath" );
            transaction.commit();
        }
        finally {
            try{
                em.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    @org.testng.annotations.Test( groups = { "jpa" }, dependsOnMethods = { "test2" } )
    public void test_createSliceKind( ) {
        SubspaceServiceImpl subspaceService = new SubspaceServiceImpl();

        SubspaceProviderImpl subspaceProvider = new SubspaceProviderImpl( emf );
        SliceKindProviderImpl sliceKindProvider = new SliceKindProviderImpl( emf );

        subspaceService.setEmf( emf );
        subspaceService.setSubspaceProvider( subspaceProvider );
        subspaceService.setSliceKindProvider( sliceKindProvider );

        subspaceService.createSliceKind( "sub", "kind", "sliceKindMode:700; uniqueDirName:kind", "root" );
    }

    @org.testng.annotations.Test( groups = { "jpa" }, dependsOnMethods = { "test_createSliceKind" } )
    public void test_createSlice( ) {
        SubspaceServiceImpl subspaceService = new SubspaceServiceImpl();
        subspaceService.init();

        SubspaceProviderImpl subspaceProvider = new SubspaceProviderImpl( emf );
        SliceKindProviderImpl sliceKindProvider = new SliceKindProviderImpl( emf );
        SliceProviderImpl sliceProvider = new SliceProviderImpl( emf );

        sliceProvider.setSubspaceProvider( subspaceProvider );
        sliceProvider.setSliceKindProvider( sliceKindProvider );

        subspaceService.setEmf( emf );
        subspaceService.setSliceProvider( sliceProvider );
        subspaceService.setSubspaceProvider( subspaceProvider );

        ResponseEntity< Specifier< Void > > response = subspaceService.createSlice("sub", "kind", "deadline:2011-12-16; sliceSize:1024", "root");

        sliceId = response.getBody().getUriMap().get( UriFactory.SLICE );
    }

    @org.testng.annotations.Test( groups = { "jpa" }, dependsOnMethods = { "test_createSlice" } )
    public void test_deleteSlice( ) throws NoSuchElementException {
        final SliceProvider sliceProvider = ( SliceProvider )context.getBean( "sliceProvider" );
        final Taskling ling = sliceProvider.deleteSlice( "sub", sliceId );
    }
}
