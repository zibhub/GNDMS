package de.zib.gndms.infra.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.Session;
import org.globus.ftp.MarkerListener;
import org.globus.ftp.GridFTPSession;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.SortedSet;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:02:37
 */
public class GNDMSFileTransfer {

    private GridFTPClient sourceClient;
    private GridFTPClient destinationClient;
    private TreeMap<String,String> files;

    private String sourcePath;
    private String destinationPath;


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
    public void setFiles( TreeMap<String,String> fm ) {
        files = fm;
    }


    /**
     * This method is provided for convenience and behaves like the above method.
     */
    public void setFiles( String sfn, String tfn ) {
        files = new TreeMap<String,String>( );
        files.put( sfn, tfn );
    }



    /**
     * Estimates the size of a prepared download or transfer.
     * @return The size in byte.
     */
    public long estimateTransferSize( ) throws IOException, ServerException {

        if( sourceClient == null )
            throw new IllegalStateException( "no source client provieded" );

        Set<String> src = files.keySet();
        long size = 0;

        sourceClient.setType( Session.TYPE_ASCII );
        sourceClient.setMode( Session.MODE_BLOCK );

        for( String s : src )
            // todo evaluate usage of msld command
            size += sourceClient.getSize( sourcePath + "/" + s );

        return size;
    }


    /**
     * Performs the prepared transfer.
     */
    public void performTransfer( MarkerListener list ) {

    }

    /**
     * Performs the prepared transfer using a persistent marker listener.
     */
    public void performPersistentTransfer( @NotNull PersistentMarkerListener plist ) throws ServerException, IOException, ClientException {

        if( sourceClient == null || destinationClient == null || files == null )
            throw new IllegalStateException( );

        setupClient ( sourceClient );
        setupClient ( destinationClient );

        sourceClient.setActive( destinationClient.setPassive() );

        Set<String> keys = files.keySet();
        for( String fn : keys ) {
            plist.setCurrentFile( fn );
            String dfn = files.get( fn );
            sourceClient.extendedTransfer( sourcePath + "/" + fn, destinationClient, destinationPath + "/" + ( dfn == null ? fn : dfn ), plist );
        }
    }


    protected void setupClient( GridFTPClient cnt ) throws ServerException, IOException {
        cnt.setType( GridFTPSession.TYPE_IMAGE );
        cnt.setMode( GridFTPSession.MODE_EBLOCK );
    }


    /**
     * Sets the source and target clients for a third party transfer.
     *
     * The clients will be configured according to their desired roles, before
     * any ftp action takes place. So don't change them after calling this method.
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


    public String getSourcePath() {
        return sourcePath;
    }


    public void setSourcePath( String sourcePath ) {
        this.sourcePath = sourcePath;
    }


    public String getDestinationPath() {
        return destinationPath;
    }


    public void setDestinationPath( String destinationPath ) {
        this.destinationPath = destinationPath;
    }
}
