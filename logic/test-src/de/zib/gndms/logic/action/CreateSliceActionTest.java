package de.zib.gndms.logic.action;

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



import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.LookupAction;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.util.SimpleModelUUIDGen;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.test.ModelEntityTestBase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * 
 * User: mjorra, Date: 14.08.2008, Time: 11:02:24
 */
@Test
public class CreateSliceActionTest extends ModelEntityTestBase {

    private final static String MY_PATH = ( "/home/mjorra/tmp/C3GridTests" );
    private SliceCreationValidator validator;
    private CreateSliceAction Action;
    private String sliceKindTestKey = "maiks-test-slice-kind";

    @BeforeTest
    public void beforeTest ( ) {

        setDbPath( MY_PATH + File.separator + "db" );

        // clean up mess form last time
        ModelUUIDGen ug = SimpleModelUUIDGen.getInstance();

        LookupAction<SliceKind, String> la = new LookupAction( SliceKind.class, sliceKindTestKey );
        la.setOwnEntityManager( getEntityManager() );
        la.setClosingEntityManagerOnCleanup( false );
        la.setUUIDGen( ug );
        getEntityManager().getTransaction().begin( );
        SliceKind sk = la.call();
        //SliceKind sk = getEntityManager().find( SliceKind.class, sliceKindTestKey );
        if( sk != null ) {
            System.out.println( "cleaning up slice kind form last test run" );
            getEntityManager().remove( sk );
        }
        getEntityManager().getTransaction().commit( );

        validator = new SliceCreationValidator();

        validator.setUuidgen( ug );
        validator.setGId( ug.nextUUID() );
        validator.setTerminationTime( new GregorianCalendar( ) );

        SliceKind knd = new SliceKind( );
        knd.setURI( sliceKindTestKey );
        knd.setPermission( AccessMask.fromString( "750" ) );
        validator.setKind(  knd );

        //getEntityManager( ) = emf.createEntityManager();
        getEntityManager( ).getTransaction().begin( );
        getEntityManager( ).persist( knd );
        getEntityManager( ).getTransaction().commit( );

        //knd = new SliceKind( );
        //knd.setPermission( SliceKindMode.RW );
        //knd.setURI( "maiks-test-slice-kind" );
        TreeSet ts = new TreeSet( );
        ts.add( knd );


        Subspace sp = new Subspace( );
        sp.setCreatableSliceKinds( ts );
        sp.setId( "welt" );
      //  LinuxDirectoryAux lda = new LinuxDirectoryAux();
      //  String spd = MY_PATH + "/subspace";
      //  lda.createSubspaceDirectory( spd );
      //  sp.setPath( spd );
        GregorianCalendar cal = new GregorianCalendar( );
        cal.add( Calendar.DAY_OF_YEAR, 20 );
//        sp.setTerminationTime( cal );

        validator.setSubspace( sp );

        //getEntityManager( ) = emf.createEntityManager();
        //getEntityManager( ).getTransaction().begin( );
        //getEntityManager( ).persist( knd );
        //getEntityManager( ).getTransaction().commit( );
        getEntityManager( ).getTransaction().begin( );
        getEntityManager( ).persist( sp );
        getEntityManager( ).getTransaction().commit( );
    }

    
    @Test(groups = { "CreateSliceTest", "LogicModelTests" })
    public void testIt() {

        DefaultBatchUpdateAction boa = new DefaultBatchUpdateAction( );
        boa.setActions( new Vector<Action<Void>>() );
        boa.setListener( new FakeEntityUpdateListener() );
        Action = validator.createCreateSliceAction(  );
        Action.setOwnPostponedEntityActions(boa);
        //getEntityManager( ).getTransaction().begin( );
        Action.setOwnEntityManager( getEntityManager( ) );
        Action.setClosingEntityManagerOnCleanup( false );
        Slice sl = Action.call();
        assert sl != null;
        // getEntityManager( ).getTransaction().commit( );

        validator.validate( sl );
        validator.validateFromDB( sl, getEntityManager() );

        getEntityManager( ).close( );
    }

    @AfterTest
    public void afterTest( ) {

        tryCloseEMF();
    }


    /*
    public static void main ( String[] args ) {

        CreateSliceActionTest csat = new CreateSliceActionTest();
        csat.beforeTest();
        csat.testIt();
        csat.afterTest( );
    }
    */
}
