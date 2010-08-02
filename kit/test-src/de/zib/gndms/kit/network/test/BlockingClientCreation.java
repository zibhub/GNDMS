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



import de.zib.gndms.kit.network.NonblockingClientFactory;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;

/**
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 20.02.2009, Time: 18:15:30
 */
public class BlockingClientCreation {

    public static void main( String[] args ) throws ServerException, IOException, InterruptedException {
        NonblockingClientFactory nbc = new NonblockingClientFactory();
        System.out.println( "precreate" );

        GridFTPClient cln = nbc.createClient( "mardschana2.zib.de", 2811, null );
//        GridFTPClient cln = nbc.createClient( "hallo", 123 );
        System.out.println( "postcreate" );
        if( cln != null )
            cln.close();

        nbc.shutdown();
        //Thread.sleep( 600000 );
    }
}
