package de.zib.gndms.kit.network.test;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 15.10.2008, Time: 17:54:50
 */
public class LittleDirectoryTransferData extends TransferTestMetaData {

    public LittleDirectoryTransferData() {
    }


    public LittleDirectoryTransferData( String sourceURI, String destinationURI ) {
        super( sourceURI, destinationURI );
    }


    public void setup() {

        HashMap<String, Long> fileSizes = new HashMap<String, Long>( );
        fileSizes.put( "b_1MB_file", new Long(    1048576) );
        setFileSizes( fileSizes );
    }
}