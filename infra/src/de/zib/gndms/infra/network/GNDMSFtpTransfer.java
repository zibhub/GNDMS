package de.zib.gndms.infra.network;

import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.Session;
import org.globus.ftp.exception.ServerException;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:02:37
 */
public class GNDMSFtpTransfer {

    private GridFTPClient sourceClient;
    private GridFTPClient destinationClient;
    private Map<String,String> fileMap;


    /**
     * Prepares the transfer of a list of files.
     *
     * @param fm
     * The given map consists of pairs of source and target file names.
     * The target file name may be null, if it should be identical to the source files name.
     *
     * @return true if
     *
     * This resets any previously prepared download stats.
     */
    public void prepareTransfer( Map<String,String> fm ) {
        fileMap = fm;
    }

    
    /**
     * This method is provided for convenience and behaves like the above method.
     */
    public void prepareTransfer( String sfn, String tfn ) {
        fileMap = new TreeMap<String,String>( );
        fileMap.put( sfn, tfn );
    }


    /**
     * Estimates the size of a prepared download or transfer.
     * @return The size in byte.
     */
    public long estimateTransferSize( ) throws IOException, ServerException {

        if( sourceClient == null )
            throw new IllegalStateException( "no source client provieded" );

        Set<String> src = fileMap.keySet();
        long size = 0;

        sourceClient.setType( Session.TYPE_ASCII );
        sourceClient.setMode( Session.MODE_BLOCK );

        for( String s : src )
            // todo evaluate useage of msld command
            size += sourceClient.getSize( s );

        return size;
    }


    /**
     * Performs the prepared transfer.
     */
    public void performTransfer( ) {

    }


    /**
     * Sets the source and target clients for a third party transfer.
     *
     * The clients will be configured according to their desired roles. 
     *
     * @param sclnt the source client.
     * @param tclnt the target client.
     */
    public void setClients(  GridFTPClient sclnt, GridFTPClient tclnt ) {

        setSourceClient( sclnt );
        setDestinationClient( tclnt );
    }


    public GridFTPClient getSourceClient() {
        return sourceClient;
    }


    public void setSourceClient( GridFTPClient sourceClient ) {
        this.sourceClient = sourceClient;
    }


    public GridFTPClient getTargetClient() {
        return destinationClient;
    }


    public void setDestinationClient( GridFTPClient destinationClient ) {
        this.destinationClient = destinationClient;
    }
}
