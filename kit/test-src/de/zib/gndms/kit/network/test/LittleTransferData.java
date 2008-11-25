package de.zib.gndms.kit.network.test;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 15.10.2008, Time: 17:54:50
 */
public class LittleTransferData extends TransferTestMetaData {

    public LittleTransferData() {
    }


    public LittleTransferData( String sourceURI, String destinationURI ) {
        super( sourceURI, destinationURI );
    }


    public void setup() {
        TreeMap<String, String> fileMap = new TreeMap<String, String>( );
        fileMap.put( "b_1MB_file", "b_1000KB_small_file" );
        setFileMap( fileMap );

        HashMap<String, Long> fileSizes = new HashMap<String, Long>( );
        fileSizes.put( "b_1MB_file", new Long(    1048576) );
        setFileSizes( fileSizes );
    }
}
