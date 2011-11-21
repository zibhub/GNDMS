package de.zib.gndmc.gridftp;
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

import de.zib.gndms.kit.network.GNDMSFileTransfer;
import org.globus.ftp.FileInfo;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.GridFTPSession;
import org.globus.ftp.Session;
import org.ietf.jgss.GSSCredential;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author try ma ik jo rr a zib
 * @date 07.06.11  18:32
 * @brief
 */
public class SizeTest extends TestBase {


    public static void main(String[] args) throws Exception {
        SizeTest cnt = new SizeTest();
        cnt.run( args );
    }


    @Override
    public void run() throws Exception {

        System.out.println( GSSCredential.DEFAULT_LIFETIME );
        super.run();
        System.out.println( "configuring client" );

        GridFTPClient cnt = getGridFTPClientProvider().provideClient();
        cnt.changeDir( path );

       // cnt.setType( GridFTPSession.TYPE_IMAGE );
       // cnt.setMode( GridFTPSession.MODE_EBLOCK );
        System.out.println( "fetching file listing" );
        Map<String,String> files = new TreeMap<String,String>( );
        Vector<FileInfo> fl = cnt.list( );
        for( FileInfo fi: fl ) {
            if( fi.isFile() ) {
                files.put( fi.getName(), null );
            }
        }

        System.out.println( "fetching size" );
        cnt.setType( Session.TYPE_ASCII );

        Set<String> src = files.keySet();
        long size = 0;

        for ( String s : src ) {
            // todo evaluate usage of msld command
            size += cnt.getSize( s );
        }

        System.out.println( "done "+ size );
    }
}
