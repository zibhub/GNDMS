package de.zib.gndms.infra.network.test;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.jetbrains.annotations.NotNull;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.apache.axis.types.URI;
import org.joda.time.DateTime;
import de.zib.gndms.logic.action.ModelEntityTestBase;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.infra.network.PersistentMarkerListener;
import de.zib.gndms.infra.network.NetworkAuxiliariesProvider;
import de.zib.gndms.infra.network.GNDMSFileTransfer;

import javax.persistence.EntityManager;
import java.util.TreeMap;
import java.util.HashMap;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 06.10.2008, Time: 11:05:48
 *
 * To run this test properly you need the transfer test data files.
 * Ask your local test data files provider for info.
 *
 * Make sure the sourceURI points to the directory containing this files.
 *
 * Commands to generate test files:
 *          dd if=/dev/urandom of=a_1KB_file bs=1k count=1
 *          dd if=/dev/urandom of=b_1MB_file bs=1k count=1024
 *          dd if=/dev/urandom of=c_1GB_file bs=1M count=1024
 */
public class TransferStateTest extends ModelEntityTestBase {

    private String sourceURI;
    private String destinationURI;
    private TreeMap<String,String> fileMap;
    private HashMap<String, Long> fileSizes;
    private final String TRANSFER_KEY = "transfer-test-a000-a000-fakekey00001";
    private FTPTransferState transferState;
    private EntityManager eM;
    private long expectedTransferSize = 0;


    @Parameters( {"srcURI", "destURI", "dbPath", "dbName" } )
    public TransferStateTest( @NotNull String srcURI, @NotNull String destURI, @NotNull String dbPath, @NotNull String dbName ) {
        super( dbPath, dbName );
        sourceURI = srcURI;
        destinationURI = destURI;
    }


    @BeforeClass( groups={ "net" } )
    public void beforeClass ( ) {

        fileMap = new TreeMap<String, String>( );
        fileMap.put( "a_1KB_file", null );
        fileMap.put( "b_1MB_file", "b_1000KB_file" );
        fileMap.put( "c_1GB_file", "c_largeFile" );

        fileSizes = new HashMap<String, Long>( );
        fileSizes.put( "a_1KB_file", new Long(       1024) );
        fileSizes.put( "b_1MB_file", new Long(    1048576) );
        fileSizes.put( "c_1GB_file", new Long( 1073741824) );

        for( String s: fileSizes.keySet()  ) {
            expectedTransferSize += fileSizes.get( s );
        }

        try{
            eM = getEntityManager();
            transferState = (FTPTransferState) eM.find( FTPTransferState.class, TRANSFER_KEY );
        } finally {
            if( eM != null )
                eM.close();
        }
    }


    @AfterClass( groups={ "net" } )
    public void afterClass ( ) {
        tryCloseEMF();
    }


    @Test( groups={ "net" } )
    public void testIt ( ) throws ServerException, IOException, ClientException {

        GridFTPClient src = null;
        GridFTPClient dest = null;
        // see NetworAuxiliariesProvider for a description of the code below
        try {
            if( transferState == null ) {
                newTransfer( );
            } else {
                System.out.println( "Resuming transfer" );
            }
            PersistentMarkerListener pml = new PersistentMarkerListener( );
            pml.setEntityManager( eM );
            pml.setTransferState( transferState );

            URI suri = new URI ( sourceURI );
            URI duri = new URI ( destinationURI );

            // obtain clients
            NetworkAuxiliariesProvider prov = new NetworkAuxiliariesProvider( );
            src = prov.getGridFTPClientFactory().createClient( suri );
            dest = prov.getGridFTPClientFactory().createClient( duri );

            // setup transfer handler
            GNDMSFileTransfer transfer = new GNDMSFileTransfer();
            transfer.setSourceClient( src );
            transfer.setSourcePath( suri.getPath() );

            transfer.setDestinationClient( dest );
            transfer.setDestinationPath( duri.getPath() );

            transfer.setFiles( fileMap );

            // estimate transfer time
            long ets = transfer.estimateTransferSize(  );
            Assert.assertEquals( ets, expectedTransferSize, "Transfer size mismatch" );

            DateTime dat = new DateTime( );
            Float tt = prov.getBandWidthEstimater().estimateBandWidthFromTo( suri.getHost( ), duri.getHost( ) );
            Assert.assertNotNull ( tt, "estimated band width" );
            System.out.println( "Estimated transfer time in sec.: "
                + NetworkAuxiliariesProvider.calculateTransferTime( ets, tt.floatValue() ) );

            System.out.println( "Starting transfer at " + (new DateTime( )).toString( ) );
            transfer.performPersistentTransfer( pml );
            System.out.println( "Finished transfer at " + (new DateTime( )).toString( ) );

        } finally {
            eM.close();
            if( src != null )
                src.close();
            if( dest != null )
                dest.close();
        }

        
    }

    
    private void newTransfer( ) {
        
        System.out.println( "Initializing new transfer" );
        transferState = new FTPTransferState();
        transferState.setTransferId( TRANSFER_KEY );
        eM.persist( transferState );
    }
}
