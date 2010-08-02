package de.zib.gndms.kit.network.test;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: try ma ik jo rr a zib
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
