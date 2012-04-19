package de.zib.gndms.taskflows.interslicetransfer.server.logic;
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


import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferResult;
import de.zib.gndms.taskflows.filetransfer.server.logic.FileTransferTaskAction;
import de.zib.gndms.taskflows.interslicetransfer.client.InterSliceTransferMeta;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:45:47
 */
public class InterSliceTransferTaskAction extends TaskFlowAction<InterSliceTransferOrder> {


    private SliceClient sliceClient;
    private SubspaceClient subspaceClient;


    public InterSliceTransferTaskAction() {
        super( InterSliceTransferMeta.INTER_SLICE_TRANSFER_KEY );
    }

    public InterSliceTransferTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }


    @Override
    public Class<InterSliceTransferOrder> getOrderBeanClass() {

        return InterSliceTransferOrder.class;
    }


    @Override
    protected void onCreated( @NotNull final String wid, @NotNull final TaskState state,
                              final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {

        final Session session = getDao().beginSession();
        try {
            final Task task = getTask( session );
            task.setPayload( new InterSliceTransferResult() );
        } finally { session.finish(); } 
        super.onCreated( wid, state, isRestartedTask,
                altTaskState );    // overriden method implementation
    }


    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state,
                                boolean isRestartedTask, boolean altTaskState) throws Exception {

        ensureOrder();
        InterSliceTransferQuoteCalculator.prepareSourceUrl( getOrder(), sliceClient );
        prepareDestination( );

        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);

            final Task st = task.createSubTask();
            st.setId(getUUIDGen().nextUUID());

            st.setTerminationTime( task.getTerminationTime() );

	        FileTransferTaskAction fta = new FileTransferTaskAction( getEmf().createEntityManager(),
                    getDao(), st.getTaskling() );

            fta.setCredentialProvider( getCredentialProvider() );
            fta.setEmf( getEmf( ) );


            fta.call( );
            if( st.getTaskState().equals( TaskState.FINISHED ) ){
                //noinspection ConstantConditions
                ( ( InterSliceTransferResult) task.getPayload() ).populate(
                        ( FileTransferResult ) st.getPayload() );
                task.setTaskState(TaskState.FINISHED);
                if (altTaskState)
                    task.setAltTaskState(null);
            }
            else
                throw (RuntimeException) st.getPayload();
            session.success();
        }
        finally { session.finish(); }
    }


    private void prepareDestination() {

        final DelegatingOrder<InterSliceTransferOrder> order = getOrder();
        final InterSliceTransferOrder orderBean = order.getOrderBean();
        
        if( orderBean.getDestinationURI() != null )
            return;

        Specifier<Void> sliceSpecifier;
        final Specifier<Void> specifier = orderBean.getDestinationSpecifier();
        if( specifier.getUriMap().containsKey( UriFactory.SLICE ) ) {
            // we got a slice specifier
            sliceSpecifier = specifier;
        } else {
            // must be a slice kind specifier
            // lets create a new slice
            ResponseEntity<Specifier<Void>> sliceSpecifierResponse =
                    subspaceClient.createSlice( specifier, order.getDNFromContext() );

            if( HttpStatus.CREATED.equals( sliceSpecifierResponse.getStatusCode() ) )
                sliceSpecifier = sliceSpecifierResponse.getBody();
            else
                throw new IllegalStateException( "Can't create slice in: " + specifier.getUrl() );
        }

        // fetch grid-ftp uri
        orderBean.setDestinationURI( getGsiFtpUrl( order, sliceSpecifier ) );

        // update task in data-base
        Session  session = getDao().beginSession();
        try {
            Task task = getTask( session );
            task.setORQ( order );
            //noinspection ConstantConditions
            ( ( InterSliceTransferResult ) task.getPayload() ).setSliceSpecifier(
                    sliceSpecifier );
            session.success();
        } finally { session.finish(); }
    }



    private String getGsiFtpUrl( final DelegatingOrder<InterSliceTransferOrder> order,
                                 final Specifier<Void> specifier )
    {

        final ResponseEntity<String> responseEntity =
                sliceClient.getGridFtpUrl( specifier,  order.getDNFromContext() );
        if( HttpStatus.OK.equals( responseEntity.getStatusCode() ) )
            return  responseEntity.getBody();
        else 
            throw new IllegalStateException( "Can't fetch gridFTP URL for slice: " +
                                             specifier.getUrl() );
    }


    public SliceClient getSliceClient() {

        return sliceClient;
    }


    @Inject
    public void setSliceClient( final SliceClient sliceClient ) {

        this.sliceClient = sliceClient;
    }


    public SubspaceClient getSubspaceClient() {

        return subspaceClient;
    }


    @Inject
    public void setSubspaceClient( final SubspaceClient subspaceClient ) {

        this.subspaceClient = subspaceClient;
    }
}
