package de.zib.gndms.kit.network.test;

import java.util.TreeMap;
import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 10:39:57
 */
public class TransferTestThreeFiles extends TransferTestMetaData {

    public TransferTestThreeFiles() {
        
    }


    public TransferTestThreeFiles( String sourceURI, String destinationURI ) {
        super( sourceURI, destinationURI );
    }


    @Override
    public void setup( ) {

        TreeMap<String, String> fileMap = new TreeMap<String, String>( );
        fileMap.put( "a_1KB_file", null );
        fileMap.put( "b_1MB_file", "b_1000KB_file" );
        fileMap.put( "c_1GB_file", "c_largeFile" );
        setFileMap( fileMap );

        HashMap<String, Long> fileSizes = new HashMap<String, Long>( );
        fileSizes.put( "a_1KB_file", new Long(       1024) );
        fileSizes.put( "b_1MB_file", new Long(    1048576) );
        fileSizes.put( "c_1GB_file", new Long( 1073741824) );
        setFileSizes( fileSizes );
    }
}
