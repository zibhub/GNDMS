package de.zib.gndms.infra.tests;

import de.zib.gndms.infra.system.SysTestBase;
import de.zib.gndms.kit.network.test.LittleTransferData;
import de.zib.gndms.kit.network.test.TransferTestMetaData;
import de.zib.gndms.logic.model.gorfx.FileTransferActionFactory;
import de.zib.gndms.logic.model.gorfx.FileTransferORQCalculator;
import de.zib.gndms.logic.model.gorfx.FileTransferORQFactory;
import de.zib.gndms.logic.model.gorfx.FileTransferTaskAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.FileTransferResult;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.globus.wsrf.ResourceException;
import org.joda.time.DateTime;
import org.testng.annotations.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 17:30:57
 */
public class FileTransferActionTest extends SysTestBase {

    Log log = LogFactory.getLog(FileTransferActionTest.class);
    TransferTestMetaData transferData;
    String  logFileConfig;
    Task task;

    @Parameters( { "srcURI", "destURI", "logFileCfg", "gridName" }  )
    public FileTransferActionTest( String srcURI, String destURI, String logFileCfg, @Optional( "c3grid" )String gridName ) {
        super( gridName );
        //transferData = new TransferTestThreeFiles( srcURI, destURI );
        transferData = new LittleTransferData( srcURI, destURI );
        logFileConfig = logFileCfg;
    }

    @BeforeClass( groups={ "net", "db", "sys", "action", "task" } )
    public void beforeClass ( ) throws ServerException, IOException, ClientException {

        PropertyConfigurator.configure( logFileConfig );
        runDatabase();
        transferData.initialize();

        // create orq
        FileTransferORQ orq = new FileTransferORQ( );
        orq.setSourceURI( transferData.getSourceURI() );
        orq.setTargetURI( transferData.getDestinationURI() );
        orq.setFileMap( transferData.getFileMap() );

        // create orq-calc
        FileTransferORQCalculator calc = new FileTransferORQCalculator();
        calc.setORQArguments( orq );
       // calc.setNetAux( getSys().getNetAux() );

        TransientContract con = calc.createOffer();
        PersistentContract pcon = con.acceptAt( new DateTime() );


        // creating offertype
        OfferType ot;
        EntityManager em = null;
        try{
            em = getSys().getEntityManagerFactory().createEntityManager();
            ot = em.find( OfferType.class, "http://www.c3grid.de/ORQTypes/FileTransfer" );
            if( ot == null ) {
                ot = createFTOfferType();
                em.getTransaction().begin( );
                em.persist( ot );
                em.getTransaction().commit( );
            }

        } finally {
            if( em != null && em.isOpen( ) )
                em.close( );
            ot = createFTOfferType();
        }

        // create task
        task = new Task( );
        task.setId( getSys().nextUUID() );
        task.setDescription( orq.getDescription() );
        task.setTerminationTime( pcon.getCurrentTerminationTime() );
        task.setOfferType( ot );
        task.setOrq( orq );
        task.setContract( pcon );
        Calendar tt = pcon.getDeadline();
        tt.add( Calendar.YEAR, 10 );
        task.setTerminationTime( tt );
    }


    @Test(groups = { "net", "db", "sys", "action", "task"})
    public void testIt( ) throws ResourceException, ExecutionException, InterruptedException {

        EntityManager em = null;
        try{
            em = getSys().getEntityManagerFactory().createEntityManager();
            em.getTransaction( ).begin( );
            em.persist( task );
            em.getTransaction( ).commit( );
            FileTransferTaskAction action = new FileTransferTaskAction( em, task );
            Future<AbstractTask> serializableFuture = getSys().submitAction(action, log);
            assert serializableFuture.get().getState().equals( TaskState.FINISHED );
            FileTransferResult ftr = ( FileTransferResult ) task.getData( );
            for( String s: Arrays.asList( ftr.getFiles( ) ) )
                System.out.println( s );

        } finally {
            if( em != null && em.isOpen( ) )
                em.close( );
        }
    }


    public static OfferType createFTOfferType( ) {
        OfferType ot = new OfferType( );
        ot.setOfferTypeKey( "http://www.c3grid.de/ORQTypes/FileTransfer" );
        ot.setOfferResultType( new ImmutableScopedName( "http://gndms.zib.de/c3grid/types", "FileTransferORQT" ) );
        ot.setOfferResultType( new ImmutableScopedName( "http://gndms.zib.de/c3grid/types", "FileTransferResultT" ) );
        ot.setCalculatorFactoryClassName( FileTransferORQFactory.class.getName() );
        ot.setTaskActionFactoryClassName( FileTransferActionFactory.class.getName( ) );
        ot.setConfigMap( new HashMap<String,String>( ) );

        return ot;
    }

    @AfterClass(groups = { "net", "db", "sys", "action", "task"})
    public void afterClass( ) {
        shutdownDatabase();
    }
}
