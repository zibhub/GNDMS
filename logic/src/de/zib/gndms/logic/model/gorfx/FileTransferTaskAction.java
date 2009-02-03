package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.FileTransferResult;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends ORQTaskAction<FileTransferORQ> {

    private FTPTransferState transferState;


    public FileTransferTaskAction() {
        super();
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull AbstractTask model ) {
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
    protected void onInProgress( @NotNull AbstractTask model ) {

        GridFTPClient src = null;
        GridFTPClient dest = null;

        try {
            EntityManager em = getEntityManager();
            transferState = em.find( FTPTransferState.class, getModel( ).getId() );

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

            TaskPersistentMarkerListener pml = new TaskPersistentMarkerListener( );
            pml.setEntityManager( em );
            pml.setTransferState( transferState );
            pml.setTask( getModel() );

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

            transfer.setFiles( files );

            transfer.prepareTransfer();

            int fc = transfer.getFiles( ).size( );
            getModel( ).setMaxProgress( fc );

            transfer.performPersistentTransfer( pml );

            em.getTransaction().begin();
            em.remove( transferState );
            em.getTransaction().commit();

            FileTransferResult ftr = new FileTransferResult();

            ArrayList<String> al = new ArrayList<String>( transfer.getFiles( ).keySet( ) );
            ftr.setFiles( al.toArray( new String[al.size( )] ));

            finish( ftr );

        } catch ( TransitException e ) {
            throw e;
        } catch ( Exception e ) {
                failWith( e );
        } finally {
     //       getEntityManager().close();
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
        transferState.setTransferId( getModel().getId() );
    }

    
    private void failWith( Exception e ) {
        fail( new IllegalStateException( e.getMessage(), e ) );
    }
}
