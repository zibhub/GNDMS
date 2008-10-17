package de.zib.gndms.kit.network.test;

import java.util.TreeMap;
import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 10:33:18
 *
 * Subclass this class and override the setup method, to provide your own data set.
 *
 * Callers of this class will only call the initialize method.
 */
public abstract class TransferTestMetaData {

    private String sourceURI;
    private String destinationURI;
    private TreeMap<String,String> fileMap;
    private HashMap<String, Long> fileSizes;
    private Long transferSize;

    protected TransferTestMetaData( ) {
    }

    protected TransferTestMetaData( String sourceURI, String destinationURI ) {
        this.sourceURI = sourceURI;
        this.destinationURI = destinationURI;
    }


    /**
     * Override this method to provide your one test data.
     * At least the file sizes and the file map should be provided.
     *
     * The source and dest uris can be set during construction.
     */
    public abstract void setup( );

    public void initialize( ) {

        setup( );

        if( sourceURI == null  )
            throw new IllegalStateException( "source uri must not be null" );
        if( destinationURI == null  )
            throw new IllegalStateException( "destination uri must not be null" );
        if( fileMap == null )
            throw new IllegalStateException( "fileMap must not be null. (Use an empty map if they should be omitted)" );
        if( fileSizes == null )
            throw new IllegalStateException( "fileSizes must not be null. (Use an empty map if they should be omitted)" );

        transferSize = new Long( 0 );
        for( String s: fileSizes.keySet()  ) {
            transferSize += fileSizes.get( s );
        }
    }


    public long expectedTransferSize ( ) {
        return transferSize;
    }
    

    public String getSourceURI() {
        return sourceURI;
    }


    protected void setSourceURI( String sourceURI ) {
        this.sourceURI = sourceURI;
    }


    public String getDestinationURI() {
        return destinationURI;
    }


    protected void setDestinationURI( String destinationURI ) {
        this.destinationURI = destinationURI;
    }


    public TreeMap<String, String> getFileMap() {
        return fileMap;
    }


    protected void setFileMap( TreeMap<String, String> fileMap ) {
        this.fileMap = fileMap;
    }


    public HashMap<String, Long> getFileSizes() {
        return fileSizes;
    }


    public void setFileSizes( HashMap<String, Long> fileSizes ) {
        this.fileSizes = fileSizes;
    }
}
