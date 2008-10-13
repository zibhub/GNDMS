package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.kit.network.PersistentMarkerListener;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.kit.network.GNDMSFileTransfer;
import org.jetbrains.annotations.NotNull;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.apache.axis.types.URI;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends ORQTaskAction<FileTransferORQ> {

    // note set new task to false when restoring a task form data base
    private FTPTransferState transferState;


    public FileTransferTaskAction() {
        super();
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull Task model ) {
        super( em, model );
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull String pk ) {
        super( em, pk );
    }


    @Override
    @NotNull
    protected Class<FileTransferORQ> getOrqClass() {
        return FileTransferORQ.class;
    }


    @Override
    protected void onInProgress( @NotNull Task model ) {

        GridFTPClient src = null;
        GridFTPClient dest = null;

        try {
            EntityManager em = getEntityManager();
            transferState = (FTPTransferState) em.find( FTPTransferState.class, getModel( ).getId() );

            TreeMap<String,String> files =  getOrq().getFileMap();
            if( transferState == null )
                newTransfer( );
            else {
                String s = transferState.getCurrentFile();
                if( s != null ) {
                    int p =  new ArrayList<String>( files.keySet() ).indexOf( s );
                    getModel().setProgress( p );
                }
            }

            PersistentMarkerListener pml = new PersistentMarkerListener( );
            pml.setEntityManager( em );
            pml.setTransferState( transferState );

            URI suri = new URI ( getOrq().getSourceURI() );
            URI duri = new URI ( getOrq().getTargetURI() );

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

            int fc = files.size( );
            getModel( ).setMax_progress( fc );

            transfer.setFiles( files );

            transfer.performPersistentTransfer( pml );

            em.remove( transferState );

            finish( fc + " files transfered."  );

        } catch ( Exception e ) {
            failWith( e );
        } finally {
            getEntityManager().close();
            try {
                if( src != null )
                    src.close();
                if( dest != null )
                    dest.close();
            } catch ( Exception e ) {
                failWith( e );
            }
        }

    }

    private void newTransfer( ) {

        transferState = new FTPTransferState();
        transferState.setTransferId( getModel().getId());
        getEntityManager().persist( transferState );
    }

    
    private void failWith( Exception e ) {
        fail( new IllegalStateException( e.getMessage() ) );
    }
}
