package de.zib.gndms.kit.network;

import de.zib.gndms.model.gorfx.FTPTransferState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.ftp.*;

import javax.persistence.EntityManager;

/**
 * A persistent marker listener for grid ftp file transfers.
 *
 * It keeps track of the restart marks and stores them into a
 * data base.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 13:27:12
 */
public class PersistentMarkerListener implements MarkerListener {

    private FTPTransferState transferState;
    private static Log logger = LogFactory.getLog( PersistentMarkerListener.class );
    private ByteRangeList byteRanges;
    private EntityManager entityManager;

    
    public PersistentMarkerListener( ) {
        byteRanges = new ByteRangeList();
    }


    public void markerArrived( Marker marker ) {

        if( marker instanceof GridFTPRestartMarker)
            gridFTPRestartMarkerArrived( ( GridFTPRestartMarker) marker );
        else if( marker instanceof PerfMarker)  {
            logger.debug( "Transfer " + transferState.getTransferId() + ": PerfMarker arrived at " +
                ( (PerfMarker) marker).getTimeStamp() );
        } else
            logger.error( "Transfer " + transferState.getTransferId() + ": Unsupported marker received." );
    }


    private void gridFTPRestartMarkerArrived( GridFTPRestartMarker marker ) {

        byteRanges.merge( marker.toVector() );
        String args = byteRanges.toFtpCmdArgument();
        try{
            logger.debug( "Transfer " + transferState.getTransferId() + " markers: " + args );;
            entityManager.getTransaction().begin();
            transferState.setFtpArgs( args );
            entityManager.getTransaction().commit();
        } finally {
            if( entityManager.getTransaction().isActive() )
                entityManager.getTransaction().rollback();
        }
    }

    
    public FTPTransferState getTransferState() {
        return transferState;
    }


    /**
     * Sets the transferStat object for this marker listener.
     *
     * If the object isn't persisted yet, this method persist it.
     * For this reason the state object needs it transferId to be set.
     */
    public void setTransferState( FTPTransferState transferState ) {

        this.transferState = transferState;

        try {
            entityManager.getTransaction().begin();
            if (! entityManager.contains( transferState ) )
                entityManager.persist( transferState );
            entityManager.getTransaction().commit();
        }
        finally {
            if ( entityManager.getTransaction().isActive() )
                entityManager.getTransaction().rollback();
        }
    }


    public boolean hasCurrentFile( )  {
        return getCurrentFile() != null;
    }
    
    
    public String getCurrentFile() {
        return transferState.getCurrentFile();
    }


    /**
     * Sets file of the transfar state to currentFile.
     *
     * Additionally the current ftp args of the state are resetted.
     * The new state is written to the database immediately.
     */
    public void setCurrentFile( String currentFile ) {

        try {
            entityManager.getTransaction().begin();
            transferState.setCurrentFile( currentFile );
            transferState.setFtpArgs( new String( ) );
            entityManager.getTransaction().commit();
        }
        finally {
            if ( entityManager.getTransaction().isActive() )
                entityManager.getTransaction().rollback();
        }
    }


    public EntityManager getEntityManager() {
        return entityManager;
    }


    public void setEntityManager( EntityManager entityManager ) {
        this.entityManager = entityManager;
    }
}
