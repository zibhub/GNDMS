package de.zib.gndms.infra.network;

import de.zib.gndms.model.gorfx.FTPTransferState;
import org.globus.ftp.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private FTPTransferState transferStat;
    private static Log logger = LogFactory.getLog( PersistentMarkerListener.class );
    private ByteRangeList byteRanges;
    private EntityManager entityManager;

    
    public PersistentMarkerListener( ) {
        byteRanges = new ByteRangeList();
    }


    public void markerArrived( Marker marker ) {

        if( marker instanceof GridFTPRestartMarker )
            gridFTPRestartMarkerArrived( ( GridFTPRestartMarker) marker );
        else if( marker instanceof PerfMarker )  {
            logger.debug( "Transfer " + transferStat.getTransferId() + ": PerfMarker arrived at " +
                ( (PerfMarker) marker).getTimeStamp() );
        } else
            logger.error( "Transfer " + transferStat.getTransferId() + ": Unsupported marker received." );
    }


    private void gridFTPRestartMarkerArrived( GridFTPRestartMarker marker ) {

        byteRanges.merge( marker.toVector() );
        String args = byteRanges.toFtpCmdArgument();
        try{
            logger.debug( "Transfer " + transferStat.getTransferId() + " markers: " + args );;
            entityManager.getTransaction().begin();
            transferStat.setFtpArgs( args );
            entityManager.getTransaction().commit();
        } finally {
            if( entityManager.getTransaction().isActive() )
                entityManager.getTransaction().rollback();
        }
    }

    
    public FTPTransferState getTransferStat() {
        return transferStat;
    }


    /**
     * Sets the transferStat object for this marker listener.
     *
     * If the object isn't persisted yet, this method persist it.
     * For this reason the state object needs it transferId to be set.
     */
    public void setTransferStat( FTPTransferState transferStat ) {

        this.transferStat = transferStat;

        try {
            entityManager.getTransaction().begin();
            if (! entityManager.contains( transferStat ) )
                entityManager.persist( transferStat );
            entityManager.getTransaction().commit();
        }
        finally {
            if ( entityManager.getTransaction().isActive() )
                entityManager.getTransaction().rollback();
        }
    }

    
    public String getCurrentFile() {
        return transferStat.getCurrentFile();
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
            transferStat.setCurrentFile( currentFile );
            transferStat.setFtpArgs( new String( ) );
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
