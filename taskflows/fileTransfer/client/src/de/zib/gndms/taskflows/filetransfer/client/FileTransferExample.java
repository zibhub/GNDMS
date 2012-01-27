/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.taskflows.filetransfer.client;

import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferResult;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 07.11.11  14:59
 * @brief
 */
public class FileTransferExample extends GORFXTaskFlowExample {

    private AbstractTaskFlowExecClient etfc;


    public static void main( String[] args ) throws Exception {

        GORFXTaskFlowExample cnt = new FileTransferExample();
        cnt.run( args );
        System.exit( 0 );
    }


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {

        etfc = new ExampleTaskFlowExecClient() {
            @Override
            protected void handleResult( TaskResult res ) {

                FileTransferResult ftr = FileTransferResult.class.cast( res );
                System.out.println( "Transferred files: " );
                for ( String file : ftr.getFiles() )
                    System.out.println( "\t" + file );
            }


            @Override
            protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

                context.addMyProxyToken( "C3GRID", "mjorra", "myproxyblah" );
                return context;
            }
        };
        return etfc;
    }


    protected void normalRun() throws Exception {

        System.out.println( "Performing normal run!!" );
        // create an order instance...
        FileTransferOrder fileTransferOrder = new FileTransferOrder();
        fileTransferOrder.setSourceURI( "gsiftp://csr-pc35.zib.de/tmp/tftest" );
        fileTransferOrder.setDestinationURI( "gsiftp://csr-pc28.zib.de/tmp/" );
        Map<String, String> fileMap = new TreeMap<String, String>();
        fileMap.put( "transferTest.dat", "transferDest.dat" );
        fileTransferOrder.setFileMap( fileMap );

        etfc.execTF( fileTransferOrder, dn, true, null );
        System.out.println( "DONE\n" );
    }


    protected void failingRun() {

    }
}
