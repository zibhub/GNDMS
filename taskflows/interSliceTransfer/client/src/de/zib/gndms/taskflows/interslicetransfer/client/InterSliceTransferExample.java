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

import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    // required from the user
    private String subspace;
    private String sliceKind;
    private FileTransferOrder order;
    private String destinationUri;
    private String localDestinationUri;

    //  The client should
    //  - create a source-slice
    //  - use inter slice transfer to copy files (external) into this slice
    //  - use a inter-slice transfer to copy the source-slice to the destination slice
    //    using the a sliceKind-specifier
    //  - use inter-slice transfer to copy the date from the destination to a local space


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {

        taskFlowExecClient = new InterSliceTransferTaskFlowExecClient();
        return taskFlowExecClient;
    }


    @Override
    protected void normalRun() throws Exception {

        //  - create a source-slice
        final ResponseEntity<Specifier<Void>> createResponse =
                subspaceClient.createSlice( subspace, sliceKind, "", dn );

        final Specifier<Void> sliceSpecifier;
        if( HttpStatus.CREATED.equals( createResponse.getStatusCode() ) )
            sliceSpecifier = createResponse.getBody();
        else
            throw new IllegalStateException( "Create slice returned: " + createResponse
                    .getStatusCode().name() );


        //  - use inter slice transfer to copy files (external) into this slice
        InterSliceTransferOrder istOrder = new InterSliceTransferOrder();
        istOrder.setSourceURI( order.getSourceURI() );
        istOrder.setFileMap( order.getFileMap() );
        istOrder.setDestinationSpecifier( sliceSpecifier );

        taskFlowExecClient.execTF( istOrder, dn );

        final Specifier<Void> importSlice = taskFlowExecClient.getResultSlice();

        //  - use a inter-slice transfer to copy the source-slice to the destination slice
        //    using the a sliceKind-specifier
        istOrder = new InterSliceTransferOrder();
        istOrder.setSourceSlice( importSlice );
        final Specifier<Void> sliceKindSpecifier = UriFactory.createSliceKindSpecifier(
                destinationUri, subspace, sliceKind );
        istOrder.setDestinationSpecifier( sliceKindSpecifier );

        taskFlowExecClient.execTF( istOrder, dn );

        final Specifier<Void> interSlice = taskFlowExecClient.getResultSlice();

        //  - use inter-slice transfer to copy the date from the destination to a local space
        istOrder = new InterSliceTransferOrder();
        istOrder.setSourceSlice( interSlice );
        istOrder.setDestinationURI( localDestinationUri );

        taskFlowExecClient.execTF( istOrder, dn );

        final Specifier<Void> resultSlice = taskFlowExecClient.getResultSlice();
    }


    @Override
    protected void failingRun() {
        // not required here
    }


    private class InterSliceTransferTaskFlowExecClient extends ExampleTaskFlowExecClient {

        public Specifier<Void> resultSlice;


        @Override
        protected void handleResult( final TaskResult res ) {

            final InterSliceTransferResult interSliceTransferResult = ( InterSliceTransferResult ) res;
            resultSlice = interSliceTransferResult.getSliceSpecifier();
        }


        public Specifier<Void> getResultSlice() {

            return resultSlice;
        }
    }
}
