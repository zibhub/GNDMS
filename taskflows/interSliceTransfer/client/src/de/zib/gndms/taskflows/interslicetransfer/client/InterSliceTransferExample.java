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

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferResult;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	private long DEFAULT_SLICE_SIZE =22222222;



    @Override
    public void run() throws Exception {

        File propFile = new File(  orderPropFile );
        if( propFile.exists() ) {
            parameter.readFromFile(propFile);
            System.out.println( "parameter getCheckOutDestinationUri: " + parameter.getCheckOutDestinationUri() );
            System.out.println( "parameter getDestinationBaseUri: " + parameter.getDestinationBaseUri() );
            System.out.println( "parameter: " + parameter.getOrder().getFileMap().toString() );
//            System.out.println( "parameter: " + parameter.getOrder().ge );
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

        subspaceClient = createBean( SubspaceClient.class );
        subspaceClient.setServiceURL( gorfxEpUrl );
        sliceClient = createBean( SliceClient.class );
        sliceClient.setServiceURL( gorfxEpUrl );

        //  - create a source-slice
        System.out.println( "Creating importSlice - create a source-slice" );
        final Specifier<Void> sliceSpecifier = createSourceSlice();
        System.out.println( "payload? " +sliceSpecifier.hasPayload());
        System.out.println( "sliceSpecifier url "+sliceSpecifier.getUrl());
        System.out.println( "sliceSpecifier getUriMap " +sliceSpecifier.getUriMap());
        System.out.println( "sliceSpecifier getUriMap " +sliceSpecifier.getUriMap().get(UriFactory.SLICE));

        System.out.println( );
        System.out.println("***************************************");
        System.out.println( );
        
        //  - use inter slice transfer to copy files (external) into this slice
        System.out.println( "Performing check-in - copy files (external) into this slice" );
        final Specifier<Void> importSlice = checkIn( sliceSpecifier );
        System.out.println( "done: " + importSlice.getUrl() );
        showFiles( importSlice );
        System.out.println( );
        System.out.println("***************************************");
        System.out.println( );
        
        //  - use a inter-slice transfer to copy the source-slice to the destination slice
        //    using the a sliceKind-specifier
        System.out.println( "Performing IST - copy the source-slice to the destination slice" );
        final Specifier<Void> interSlice = interSliceTransfer( importSlice );
        System.out.println( "done: " + importSlice.getUrl() );
        System.out.println( );
        System.out.println("***************************************");
        System.out.println( );

        //  - use inter-slice transfer to copy the date from the destination to a local space
        System.out.println( "Performing check-out - copy the date from the destination to a local space" );
        List<String> files = checkOut( interSlice );
        System.out.println( "Received: " );
        for( String f : files ) 
            System.out.println( "    " + f );
        System.out.println( );
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
        System.out.println("parameter.getOrder().getSourceURI() " +  parameter.getOrder().getSourceURI());
//        System.out.println(" parameter.getOrder().getFileMap() "+ parameter.getOrder().getFileMap().get(0));
        System.out.println(" parameter.getOrder().getFileMap() "+ parameter.getOrder().getFileMap());
        System.out.println("source Slice id "+ sliceSpecifier.getUriMap().get( UriFactory.SLICE ));
        istOrder.setSourceSlice(sliceSpecifier);
        istOrder.setDestinationSpecifier( sliceSpecifier );

        taskFlowExecClient.execTF( istOrder, dn );
        System.out.println("taskFlowExecClient.getResultSlice() "+ taskFlowExecClient.getResultSlice());

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
        System.out.println("importSlice " +importSlice.getUrl());

        
        final Specifier<Void> sliceKindSpecifier = UriFactory.createSliceKindSpecifier(
                parameter.getDestinationBaseUri(),
                parameter.getSubspace(),
                parameter.getSliceKind() );
        istOrder.setDestinationSpecifier( sliceKindSpecifier );

        System.out.println("sliceKindSpecifier " +sliceKindSpecifier.getUrl());
        taskFlowExecClient.execTF( istOrder, dn );
        
        System.out.println("taskFlowExecClient.getResultSlice()" +taskFlowExecClient.getResultSlice());

        return taskFlowExecClient.getResultSlice();
    }


    /**
     * Creates and executes an check-in IST order.
     *
     * For the check-out the source-location is an existing slice and the destination
     * is set to an arbitrary GridFTP url.
     *
     * @param sliceSpecifier The source slice specifier
     * @return A list of transferred files
     */
    private List<String> checkOut( final Specifier<Void> interSlice ) {

        InterSliceTransferOrder istOrder = new InterSliceTransferOrder();
        istOrder.setSourceSlice( interSlice );
        istOrder.setDestinationURI( parameter.getCheckOutDestinationUri() );
        istOrder.setExpectedSliceSize(parameter.getExpectedSliceSize());
        System.out.println(" parameter.getCheckOutDestinationUri()"+ parameter.getCheckOutDestinationUri());
        System.out.println("parameter.getExpectedSliceSize()"+parameter.getExpectedSliceSize());
        
        taskFlowExecClient.execTF( istOrder, dn );

        return taskFlowExecClient.getResult().getFiles();
    }

	private Specifier<Void> createSourceSlice() {

		SliceConfiguration sconf = new SliceConfiguration();

		if (getDesiredQuote()!=null && getDesiredQuote().getExpectedSize() != null) {
			logger.debug("createSlice desiredQuote "
					+ getDesiredQuote().getExpectedSize());
		}

		if (parameter.getExpectedSliceSize() != null
				&& Long.parseLong(parameter.getExpectedSliceSize().trim()) > 0) {
			sconf.setSize(Long.parseLong(parameter.getExpectedSliceSize()
					.trim()));
		} else {
			logger.debug("no slice size provided, setting slice size to the default value "
					+ DEFAULT_SLICE_SIZE);
			System.out
					.println("no slice size provided, setting slice size to the default value "
							+ DEFAULT_SLICE_SIZE);
			sconf.setSize(DEFAULT_SLICE_SIZE);
		}

		final ResponseEntity<Specifier<Void>> createResponse = subspaceClient
				.createSlice(parameter.getSubspace(), parameter.getSliceKind(),
						sconf.getStringRepresentation(), dn);

		if (HttpStatus.CREATED.equals(createResponse.getStatusCode()))
			return createResponse.getBody();
		else
			throw new IllegalStateException("Create slice returned: "
					+ createResponse.getStatusCode().name());
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


        @Override
        protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

            context.addMyProxyToken( "C3GRID", myProxyLogin, myProxyPasswd );
            return context;
        }


        public InterSliceTransferResult getResult() {

            return result;
        }


        public Specifier<Void> getResultSlice() {

            return result.getSliceSpecifier();
        }
    }



    public static void main( String[] args ) throws Exception {
        System.out.println( "   InterSliceTransferExample ");
        GORFXTaskFlowExample cnt = new InterSliceTransferExample();
        System.out.println( "   GORFXTaskFlowExample created  ");
        cnt.run( args );
        System.exit( 0 );
    }
}
