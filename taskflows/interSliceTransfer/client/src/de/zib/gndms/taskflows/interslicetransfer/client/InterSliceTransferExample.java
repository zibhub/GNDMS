package de.zib.gndms.taskflows.interslicetransfer.client;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

/**
 * Stub for a inter-slice transfer client
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 17.02.12  17:38
 * @brief
 */
public class InterSliceTransferExample extends GORFXTaskFlowExample {


    private SubspaceClient subspaceClient;
    private SliceClient sliceClient;
    private InterSliceTransferTaskFlowExecClient taskFlowExecClient;

    private final InterSliceTransferExampleBean parameter = new InterSliceTransferExampleBean();


    @Override
    public void run() throws Exception {

        File propFile = new File(  orderPropFile );
        if( propFile.exists() ) {
            parameter.readFromFile( propFile );
            subspaceClient = createBean( SubspaceClient.class );
            subspaceClient.setServiceURL( gorfxEpUrl );
            sliceClient = createBean( SliceClient.class );
            sliceClient.setServiceURL( gorfxEpUrl );
            super.run();
        } else  {
            System.out.println( "Creating property template in: " + orderPropFile );
            parameter.createPropsTemplate( propFile );
        }
    }


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {

        taskFlowExecClient = new InterSliceTransferTaskFlowExecClient();
        return taskFlowExecClient;
    }


    /**
     * A test-run for all possible use-cases of the IST.
     *
     *  The run consists of:
     *
     *  - create a source-slice
     *  - use inter slice transfer to copy files (external) into this slice
     *  - use a inter-slice transfer to copy the source-slice to the destination slice
     *    using the a sliceKind-specifier
     *  - use inter-slice transfer to copy the date from the destination to a local space
     *
     * @throws Exception
     */
    @Override
    protected void normalRun() throws Exception {

        //  - create a source-slice
        System.out.println( "Creating importSlice" );
        final Specifier<Void> sliceSpecifier = createSourceSlice();


        //  - use inter slice transfer to copy files (external) into this slice
        System.out.println( "Performing check-in" );
        final Specifier<Void> importSlice = checkIn( sliceSpecifier );
        System.out.println( "done: " + importSlice.getUrl() );
        showFiles( importSlice );

        //  - use a inter-slice transfer to copy the source-slice to the destination slice
        //    using the a sliceKind-specifier
        System.out.println( "Performing IST" );
        final Specifier<Void> interSlice = interSliceTransfer( importSlice );
        System.out.println( "done: " + importSlice.getUrl() );

        //  - use inter-slice transfer to copy the date from the destination to a local space
        System.out.println( "Performing check-out" );
        List<String> files = checkOut( interSlice );
        System.out.println( "Received: " );
        for( String f : files ) 
            System.out.println( "    " + f );
    }


    private void showFiles( final Specifier<Void> slice ) {

        final ResponseEntity<List<FileStats>> listResponseEntity =
                sliceClient.listFiles( slice , dn );

        if ( HttpStatus.OK.equals( listResponseEntity.getStatusCode() ) ) {
            final List<FileStats> fileStats = listResponseEntity.getBody();
            System.out.println( "   transferred " + fileStats.size() + " files" );
            for( FileStats fs : fileStats )
                System.out.println( "    " + fs );
        }
    }


    /**
     * Creates and executes an check-in IST order.
     *
     * For the check-in source-location is set to an arbitrary GridFTP url,
     * and the destination is a slice.
     *
     * @param sliceSpecifier The source slice specifier
     * @return The target slice specifier (equal to \t sliceSpecifier in this example)
     */
    private Specifier<Void> checkIn( final Specifier<Void> sliceSpecifier ) {

        InterSliceTransferOrder istOrder = new InterSliceTransferOrder();
        istOrder.setSourceURI( parameter.getOrder().getSourceURI() );
        istOrder.setFileMap( parameter.getOrder().getFileMap() );
        istOrder.setDestinationSpecifier( sliceSpecifier );

        taskFlowExecClient.execTF( istOrder, dn );

        return taskFlowExecClient.getResultSlice();
    }


    /**
     * Creates and executes a classical IST order.
     *
     * Here the source is an existing slice and the destination is a sliceKind specifier, i.e.
     * the destination slice will be created.
     *
     * @param sliceSpecifier The source slice specifier
     * @return The target slice specifier.
     */
    private Specifier<Void> interSliceTransfer( final Specifier<Void> importSlice ) {

        InterSliceTransferOrder istOrder = new InterSliceTransferOrder();
        istOrder.setSourceSlice( importSlice );
        final Specifier<Void> sliceKindSpecifier = UriFactory.createSliceKindSpecifier(
                parameter.getDestinationBaseUri(),
                parameter.getSubspace(),
                parameter.getSliceKind() );
        istOrder.setDestinationSpecifier( sliceKindSpecifier );

        taskFlowExecClient.execTF( istOrder, dn );

        return taskFlowExecClient.getResultSlice();
    }


    /**
     * Creates and executes an check-in IST order.
     *
     * For the check-out the source-location is an existing slice and the desitionation
     * is set to an arbitrary GridFTP url.
     *
     * @param sliceSpecifier The source slice specifier
     * @return A list of transferred files
     */
    private List<String> checkOut( final Specifier<Void> interSlice ) {

        InterSliceTransferOrder istOrder = new InterSliceTransferOrder();
        istOrder.setSourceSlice( interSlice );
        istOrder.setDestinationURI( parameter.getCheckOutDestinationUri() );

        taskFlowExecClient.execTF( istOrder, dn );

        return taskFlowExecClient.getResult().getFiles();
    }


    private Specifier<Void> createSourceSlice() {

        final ResponseEntity<Specifier<Void>> createResponse =
                subspaceClient.createSlice( parameter.getSubspace(),
                        parameter.getSliceKind(), "", dn );

        if( HttpStatus.CREATED.equals( createResponse.getStatusCode() ) )
            return createResponse.getBody();
        else
            throw new IllegalStateException( "Create slice returned: " + createResponse
                    .getStatusCode().name() );
    }


    @Override
    protected void failingRun() {
        // not required here
    }


    private class InterSliceTransferTaskFlowExecClient extends ExampleTaskFlowExecClient {


        private InterSliceTransferResult result;


        @Override
        public void handleResult( final TaskResult res ) {

            result = ( InterSliceTransferResult ) res;
        }

        public InterSliceTransferResult getResult() {

            return result;
        }


        public Specifier<Void> getResultSlice() {

            return result.getSliceSpecifier();
        }
    }



    public static void main( String[] args ) throws Exception {

        GORFXTaskFlowExample cnt = new InterSliceTransferExample();
        cnt.run( args );
        System.exit( 0 );
    }
}
