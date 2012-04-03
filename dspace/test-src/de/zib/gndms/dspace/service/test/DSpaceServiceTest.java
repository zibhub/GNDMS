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

package de.zib.gndms.dspace.service.test;

import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.dspace.service.SubspaceServiceImpl;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.NoWSDontNeedModelUpdateListener;
import de.zib.gndms.logic.model.dspace.*;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.test.ModelEntityTestBase;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: explicit
 * Date: 02.12.11
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class DSpaceServiceTest extends ModelEntityTestBase
{
    String sliceId;
    
    final static String subspaceId = "sub";
    final static String subspacePath = "/tmp/gndms/sub";
    final static String gridFtpPath = "gridFtpPath";
    
    final static String sliceKindId = "kind";
    final static String sliceKindConfig = "sliceKindMode:700; uniqueDirName:kind";
    
    final static String sliceConfig = "terminationTime:2011-12-16; sliceSize:1024";
    
    final static String dn = "root";


    @Parameters( { "dbPath", "dbName"} )
    public DSpaceServiceTest(String dbPath, @Optional("c3grid") String dbName) {
        super( dbPath, dbName );
    }


    @org.testng.annotations.Test(
            groups = { "dspaceActionTests", "modeltests" }
    )
    public void testSetupSubspaceAction() {
        TxFrame txFrame = new TxFrame( getEntityManager() );

        SubspaceConfiguration subspaceConfig = new SubspaceConfiguration(
                subspacePath,
                gridFtpPath,
                true,
                100,
                SetupMode.CREATE,
                subspaceId
        );
        SetupSubspaceAction action = new SetupSubspaceAction( subspaceConfig );

        StringWriter sw = new StringWriter();

        action.setClosingEntityManagerOnCleanup( false );
        action.setOwnEntityManager( getEntityManager() );
        action.setPrintWriter( new PrintWriter( sw ) );
        logger.info( "Calling action for setting up the supspace " + subspaceId + ".");
        action.setOwnPostponedEntityActions( new DefaultBatchUpdateAction< GridResource >() );
        action.getPostponedEntityActions().setListener( new NoWSDontNeedModelUpdateListener() );
        action.call();

        txFrame.commit();
        txFrame.finish();
    }


    @org.testng.annotations.Test(
            groups = { "dspaceServiceTests", "modeltests" },
            dependsOnMethods = { "testSetupSubspaceAction" }
    )
    public void testFindSubspaceById() {
        TxFrame txFrame = new TxFrame( getEntityManager() );

        Subspace sub = getEntityManager().find( Subspace.class, subspaceId );
        Assert.assertNotNull( sub );
        Assert.assertEquals( sub.getGsiFtpPath(), gridFtpPath );
        
        txFrame.commit();
        txFrame.finish();
    }


    @org.testng.annotations.Test(
            groups = { "dspaceServiceTests", "modeltests" },
            dependsOnMethods = { "testSetupSubspaceAction" }
    )
    public void testCreateSliceKindByProviders( ) {
        final SubspaceServiceImpl subspaceService = new SubspaceServiceImpl();

        final SubspaceProviderImpl subspaceProvider = new SubspaceProviderImpl( getEntityManagerFactory() );
        final SliceKindProviderImpl sliceKindProvider = new SliceKindProviderImpl( getEntityManagerFactory() );

        subspaceService.setEmf( getEntityManagerFactory() );
        subspaceService.setSubspaceProvider( subspaceProvider );
        subspaceService.setSliceKindProvider( sliceKindProvider );

        subspaceService.createSliceKind(
                subspaceId,
                sliceKindId,
                sliceKindConfig,
                dn
        );
        
        SliceKind sk = getEntityManager().find( SliceKind.class, sliceKindId );
        Assert.assertNotNull( sk );
    }


    @org.testng.annotations.Test(
            groups = { "dspaceServiceTests", "modeltests" },
            dependsOnMethods = { "testCreateSliceKindByProviders" }
    )
    public void testCreateSliceByService( ) {
        final SubspaceServiceImpl subspaceService = new SubspaceServiceImpl();
        subspaceService.init();

        final SubspaceProviderImpl subspaceProvider = new SubspaceProviderImpl( getEntityManagerFactory() );
        final SliceKindProviderImpl sliceKindProvider = new SliceKindProviderImpl( getEntityManagerFactory() );
        final SliceProviderImpl sliceProvider = new SliceProviderImpl( getEntityManagerFactory() );

        sliceProvider.setSubspaceProvider( subspaceProvider );
        sliceProvider.setSliceKindProvider( sliceKindProvider );

        subspaceService.setEmf( getEntityManagerFactory() );
        subspaceService.setSliceProvider( sliceProvider );
        subspaceService.setSubspaceProvider( subspaceProvider );

        final ResponseEntity< Specifier< Void > > response = subspaceService.createSlice(
                subspaceId,
                sliceKindId,
                sliceConfig,
                dn
        );

        Assert.assertEquals( response.getStatusCode(), HttpStatus.CREATED );

        sliceId = response.getBody().getUriMap().get( UriFactory.SLICE );
    }


    @org.testng.annotations.Test(
            groups = { "dspaceServiceTests", "modeltests" },
            dependsOnMethods = { "testCreateSliceByService" }
    )
    public void testDeleteSlice( ) throws Exception {
        final SubspaceProviderImpl subspaceProvider = new SubspaceProviderImpl( getEntityManagerFactory() );
        final SliceKindProviderImpl sliceKindProvider = new SliceKindProviderImpl( getEntityManagerFactory() );
        final SliceProviderImpl sliceProvider = new SliceProviderImpl( getEntityManagerFactory() );

        sliceProvider.setSubspaceProvider( subspaceProvider );
        sliceProvider.setSliceKindProvider( sliceKindProvider );

        final Taskling ling = sliceProvider.deleteSlice( subspaceId, sliceId );

        while( true ) {
            Thread.sleep( 100 );

            TaskState taskState;
            Session session = ling.getDao().beginSession();

            try {
                Task task = ling.getTask( session );
                taskState = task.getTaskState();

                if( TaskState.FAILED.equals( taskState ) ) {
                    throw task.getCause().get( 0 );
                }
                if( taskState.isDoneState() ) {
                    session.success();
                    break;
                }

                logger.info( "TaskState: " + taskState.toString() );
                System.out.println( "TaskState: " + taskState.toString() );

                session.success();
            }
            finally {
                session.finish();
            }
        }
        
        Slice slice = getEntityManager().find( Slice.class, sliceId );
        Assert.assertNull( slice );
    }
}
