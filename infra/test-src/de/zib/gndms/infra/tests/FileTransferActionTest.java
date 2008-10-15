package de.zib.gndms.infra.tests;

import de.zib.gndms.infra.system.SysTestBase;
import de.zib.gndms.kit.network.test.TransferTestMetaData;
import de.zib.gndms.kit.network.test.TransferTestThreeFiles;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.logic.model.gorfx.FileTransferORQCalculator;
import de.zib.gndms.logic.model.gorfx.FileTransferTaskAction;
import org.testng.annotations.*;
import org.apache.log4j.PropertyConfigurator;
import org.globus.ftp.exception.ServerException;
import org.globus.wsrf.ResourceException;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 17:30:57
 */
public class FileTransferActionTest extends SysTestBase {

    TransferTestMetaData transferData;
    String  logFileConfig;
    Task task;

    @Parameters( { "srcURI", "destURI", "logFileCfg", "gridName" }  )
    public FileTransferActionTest( String srcURI, String destURI, String logFileCfg, @Optional( "c3grid" )String gridName ) {
        super( gridName );
        transferData = new TransferTestThreeFiles( srcURI, destURI );
        logFileConfig = logFileCfg;
    }

    @BeforeClass( groups={ "net", "db", "sys", "action", "task" } )
    public void beforeClass ( ) throws ServerException, IOException {

        PropertyConfigurator.configure( logFileConfig );
        transferData.initialize();

        // create orq
        FileTransferORQ orq = new FileTransferORQ( );
        orq.setSourceURI( transferData.getSourceURI() );
        orq.setTargetURI( transferData.getDestinationURI() );
        orq.setFileMap( transferData.getFileMap() );

        // create orq-calc
        FileTransferORQCalculator calc = new FileTransferORQCalculator();
        calc.setORQArguments( orq );

        Contract con = calc.createOffer();

        // creating offertype
        // todo ask stefan

        // create task
        task = new Task( );
        task.setId( getSys().nextUUID() );
        task.setDescription("Dummy");
        task.setTerminationTime( con.getResultValidity());
        task.setOfferType( null );
        task.setOrq( orq );
        task.setContract( con );
        Calendar tt = con.getDeadline();
        tt.add( Calendar.YEAR, 10 );
        task.setTerminationTime( tt );
    }


    @Test(groups = { "net", "db", "sys", "action", "task"})
    public void testIt( ) throws ResourceException, ExecutionException, InterruptedException {

        runDatabase();
        final EntityManager em = getSys().getEntityManagerFactory().createEntityManager();
        FileTransferTaskAction action = new FileTransferTaskAction( em, task );
        final Future<Task> serializableFuture = getSys().submitAction(action);
        assert serializableFuture.get().getState().equals( TaskState.FINISHED );
    }

    @AfterClass(groups = { "net", "db", "sys", "action", "task"})
    public void afterClass( ) {
        shutdownDatabase();
    }
}
