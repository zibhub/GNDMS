/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.dmsstaging.server.logic;

import de.zib.gndms.common.model.gorfx.types.TaskFlowFailure;
import de.zib.gndms.common.model.gorfx.types.TaskFlowStatus;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.gorfx.GORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.stuff.misc.LanguageAlgebra;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInMeta;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.voldmodel.Adis;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @date: 19.06.12
 * @time: 11:01
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInTaskAction extends TaskFlowAction< DmsStageInOrder > {
    private Adis adis;
    private TaskFlowClient taskFlowClient;
    private GORFXClient gorfxClient;

    public DmsStageInTaskAction( ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY );
    }


    DmsStageInTaskAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY, em, dao, model );
    }


    @Override
    protected void onCreated( @NotNull String wid, @NotNull TaskState state,
                              boolean isRestartedTask, boolean altTaskState )
            throws Exception
    {
        ensureOrder();
        
        GORFXClient tmpGorfxClient = new GORFXClient( getOrderBean().getGridSite() );
        tmpGorfxClient.setRestTemplate( gorfxClient.getRestTemplate() );

        final ResponseEntity< Specifier< Facets > > responseEntity = tmpGorfxClient.createTaskFlow(
                ProviderStageInMeta.PROVIDER_STAGING_KEY,
                getOrderBean(),
                getOrder().getDNFromContext(),
                getOrder().getActId(),
                LanguageAlgebra.getMultiValueMapFromMap(getOrder().getActContext() ) );
        
        getOrderBean().setWorkflowId( responseEntity.getBody().getUriMap().get( "id" ) );
    }


    @Override
    protected void onInProgress(@NotNull String wid,
                                @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception
    {
        ensureOrder();
        TaskFlowClient tmpTaskFlowClient = new TaskFlowClient( getOrderBean().getGridSite() );
        tmpTaskFlowClient.setRestTemplate( taskFlowClient.getRestTemplate() );
        
        Integer updateInterval = 1000;
        
        while( true ) {
            final ResponseEntity< TaskFlowStatus > status = taskFlowClient.getStatus(
                    ProviderStageInMeta.PROVIDER_STAGING_KEY,
                    getOrderBean().getWorkflowId(),
                    getOrder().getDNFromContext(),
                    getOrder().getActId() );

            if( status.getBody().getState().equals( TaskFlowStatus.State.TASK_DONE ) ) {
                
                if( status.getBody().getTaskStatus().getStatus().equals( TaskStatus.Status.FAILED ) ) {
                    
                    transit( TaskState.FAILED );
                    final ResponseEntity< TaskFlowFailure > errors = tmpTaskFlowClient.getErrors(
                            ProviderStageInMeta.PROVIDER_STAGING_KEY,
                            getOrderBean().getWorkflowId(),
                            getOrder().getDNFromContext(),
                            getOrder().getActId() );
                    failWithPayload(null, new Exception(errors.getBody().getFailureMessage()));
                }
                else if( status.getBody().getTaskStatus().getStatus().equals( TaskStatus.Status.FINISHED ) ) {

                    final ResponseEntity< Specifier< TaskResult > > responseEntity = tmpTaskFlowClient.getResult(
                            ProviderStageInMeta.PROVIDER_STAGING_KEY,
                            getOrderBean().getWorkflowId(),
                            getOrder().getDNFromContext(),
                            getOrder().getActId() );

                    transitWithPayload( responseEntity.getBody().getPayload(), TaskState.FINISHED );
                }

                break;
            }
            
            Thread.sleep( updateInterval );
        }
    }



    @Override
    public Class< DmsStageInOrder > getOrderBeanClass( ) {
        return DmsStageInOrder.class;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setAdis( final Adis adis ) {
        this.adis = adis;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setTaskFlowClient( TaskFlowClient taskFlowClient ) {
        this.taskFlowClient = taskFlowClient;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setGorfxClient( GORFXClient gorfxClient ) {
        this.gorfxClient = gorfxClient;
    }
}
