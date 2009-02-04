package de.zib.gndms.kit.network;

import de.zib.gndms.model.gorfx.FTPTransferState;
import org.globus.ftp.*;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.FTPException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
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


    public TreeMap<String, String> getFiles() {
        return files;
    }


    /**
     * This method can be called before the actual transfer is performed.
     *
     * It ensures that the file set isn't empty. If it is empty the file listing
     * will be requested from the source client.
     *
     * This is useful if one would like to see the file listing befor the transfer starts.
     */
    public void prepareTransfer( ) throws ServerException, IOException, ClientException {

        if( sourceClient == null )
            throw new IllegalStateException( enrichExceptionMsg( "no source client provided" ) );

        try {
            if( sourcePath != null )
                sourceClient.changeDir( sourcePath );

            if( files == null || files.size( ) == 0  )
                fetchFileListing();
        } catch ( ServerException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage() ) );
            throw ex;
        } catch ( ClientException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage() ) );
            throw ex;
        } catch ( IOException ex ) {
            IOException ioe = new IOException( enrichExceptionMsg( ex.getMessage() ) );
            ioe.setStackTrace( ex.getStackTrace() );
            throw ioe;
        }
    }


    /**
     * Estimates the size of a prepared download or transfer.
     * @return The size in byte.
     */
    public long estimateTransferSize( ) throws IOException, ServerException, ClientException {

        String nm = null;
        try {
            prepareTransfer( );

            sourceClient.setType( Session.TYPE_ASCII );

            Set<String> src = files.keySet();
            long size = 0;

            for( String s : src ) {
                // todo evaluate usage of msld command
                nm = s;
                size += sourceClient.getSize( s );
            }

            return size;
        } catch ( ServerException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage(), nm ) );
            throw ex;
        } catch ( ClientException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage(), nm ) );
            throw ex;
        } catch ( IOException ex ) {
            IOException ioe = new IOException( enrichExceptionMsg( ex.getMessage(), nm ) );
            ioe.setStackTrace( ex.getStackTrace() );
            throw ioe;
        }
    }


    /**
     * Performs the prepared transfer.
     *
     * NOTE:  not implemented yet.
     */
    public void performTransfer( MarkerListener list ) {

    }

    /**
     * Performs the prepared transfer using a persistent marker listener.
     *
     * If the listener has a state, i.e. a currentFile and a range, then the transfer will uses this state to continue
     * the transfer.
     */
    public void performPersistentTransfer( @NotNull PersistentMarkerListener plist ) throws ServerException, IOException, ClientException {

        String nm = null;
        try {
            prepareTransfer( );

            if( destinationClient == null || files == null )
                throw new IllegalStateException( );

            setupClient ( sourceClient );

            setupClient ( destinationClient );
            destinationClient.changeDir( destinationPath );

            sourceClient.setActive( destinationClient.setPassive() );

            // todo beautify the code below
            boolean resume = plist.hasCurrentFile();
            String  rfn = plist.getCurrentFile();

            Set<String> keys = files.keySet();
            for( String fn : keys ) {
                nm = fn;
                if( resume && fn.equals( rfn ) ) {
                    resume = false;
                    resumeSource( plist.getTransferState() );
                }

                if( !resume ) {
                    plist.setCurrentFile( fn );
                    String dfn = files.get( fn );
                    sourceClient.extendedTransfer( fn, destinationClient, ( dfn == null ? fn : dfn ), plist );
                }
            }
        } catch ( ServerException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage(), nm ) );
            throw ex;
        } catch ( ClientException ex ) {
            ex.setCustomMessage( enrichExceptionMsg( ex.getMessage(), nm ) );
            throw ex;
        } catch ( IOException ex ) {
            IOException ioe = new IOException( enrichExceptionMsg( ex.getMessage(), nm ) );
            ioe.setStackTrace( ex.getStackTrace() );
            throw ioe;
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


    /**
     * This loads the ftp byte range args from a FTPTransferState object into a GridFTPClient.
     *
     */
    private void resumeSource( FTPTransferState stat ) throws ServerException, IOException {

        ByteRangeList brl = new ByteRangeList();
        GridFTPRestartMarker rm = new GridFTPRestartMarker( stat.getFtpArgsString( ) );
        brl.merge( rm.toVector() );
        sourceClient.setRestartMarker( brl );
    }

    
    private void fetchFileListing( ) throws ClientException, ServerException, IOException {

        files = new TreeMap<String,String>( );
        Vector<FileInfo> inf = sourceClient.list( );
        for( FileInfo fi: inf ) {
            if( fi.isFile() ) {
                files.put( fi.getName(), null );
            }
        }
    }


    private String enrichExceptionMsg( String msg ) {
        return enrichExceptionMsg( msg, null );
    }


    private String enrichExceptionMsg( String msg, String fn ) {

        StringWriter nmsg = new StringWriter(  );
        nmsg.write( "Tansfer" );
        if( fn != null ) {
            nmsg.write( " with file " +fn );
        }

        nmsg.write( " from " +
            printWithNull( sourceClient )
            + "/" + printWithNull( sourcePath ) );

        nmsg.write( " to " +
            printWithNull( destinationClient )
                + "/" + printWithNull( destinationPath ) );

        nmsg.write( "\nan Exception occured: " + msg );

        return nmsg.toString( );
    }

    static private String printWithNull( Object o ) {
        return o == null ? "<null>" : o.toString() ;
    }
}
