package de.zib.gndms.kit.network.test;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.TreeMap;
import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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
