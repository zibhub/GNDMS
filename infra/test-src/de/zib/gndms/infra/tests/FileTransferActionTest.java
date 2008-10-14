package de.zib.gndms.infra.tests;

import de.zib.gndms.infra.system.SysTestBase;
import de.zib.gndms.kit.network.test.TransferTestMetaData;
import de.zib.gndms.kit.network.test.TransferTestThreeFiles;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.logic.model.gorfx.FileTransferORQCalculator;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.apache.log4j.PropertyConfigurator;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;

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

    @BeforeClass( groups={ "net" } )
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

        // create task
        task.setDescription("Dummy");
        task.setTerminationTime( con.getResultValidity());
        task.setOfferType( orq.getOfferType() );
        task.setOrq("null");
        task.setContract( con );
        
    }


}
