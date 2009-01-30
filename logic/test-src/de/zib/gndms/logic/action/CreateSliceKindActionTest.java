package de.zib.gndms.logic.action;

import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.LookupAction;
import de.zib.gndms.logic.model.dspace.CreateSliceKindAction;
import de.zib.gndms.logic.util.SimpleModelUUIDGen;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.test.ModelEntityTestBase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Vector;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.08.2008, Time: 17:50:56
 */
public class CreateSliceKindActionTest extends ModelEntityTestBase {

    private final String URI = "yatsk";
    private final static String MY_PATH = "/home/mjorra/tmp/C3GridTests";
    private static final ModelUUIDGen uuidgen = SimpleModelUUIDGen.getInstance();

    @BeforeTest
    public void BeforeTest( ) {

        setDbPath( MY_PATH + File.separator + "db" );
        // clean up mess form last time

        LookupAction<SliceKind, String> la = new LookupAction( SliceKind.class, URI );
        la.setOwnEntityManager( getEntityManager() );
        la.setUUIDGen( uuidgen );
        getEntityManager().getTransaction().begin( );
        SliceKind sk = la.call();
        //sk = getEntityManager().find( SliceKind.class, sliceKindTestKey );
        if( sk != null ) {
            System.out.println( "cleaning up slice kind form last test run" );
            getEntityManager().remove( sk );
        }
        getEntityManager().getTransaction().commit( );
    }

    @Test(groups = { "CreateSliceTest", "LogicModelTests" })
    public void testIt() {

        EntityManager em = getEntityManager();
        DefaultBatchUpdateAction boa = new DefaultBatchUpdateAction( );
        boa.setActions( new Vector<Action<Void>>() );
        boa.setListener( new FakeEntityUpdateListener() );

        SliceKindCreationValidator val = new SliceKindCreationValidator( );
        val.setURI( URI );
        val.setPermission( AccessMask.fromString( "550" ) );
        CreateSliceKindAction action =  val.createCreateSliceAction();
        action.setOwnEntityManager( em );
        action.setOwnPostponedActions( boa );
        action.setUUIDGen( uuidgen );
        SliceKind sl = action.call();
        val.validate(  sl  );
        val.validateFromDB( sl, em );
    }
    
    @AfterTest
    public void afterTest( ) {

        tryCloseEMF();
    }
}
