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

import de.zib.gndms.common.model.gorfx.types.*;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.GORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
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
import java.util.Map;

/**
 * @date: 19.06.12
 * @time: 11:01
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInTaskAction extends TaskFlowAction< DmsStageInOrder > {
    private Dao dao;
    private Adis adis;
    private TaskFlowClient taskFlowClient;
    private TaskClient taskClient;
    private GORFXClient gorfxClient;
    
    private Quote acceptedQuote;

    public DmsStageInTaskAction( ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY );
    }


    public DmsStageInTaskAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY, em, dao, model );
    }


    @Override
    public void onCreated( @NotNull String wid, @NotNull TaskState state,
                              boolean isRestartedTask, boolean altTaskState )
            throws Exception
    {
        ensureOrder();

        super.onCreated( wid, state,  isRestartedTask,  altTaskState );
    }


    @Override
    public void onInProgress(@NotNull String wid,
                                @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception
    {
        ensureOrder();

        GORFXClient tmpGorfxClient = new GORFXClient( getAcceptedQuote().getSite() );
        tmpGorfxClient.setRestTemplate( gorfxClient.getRestTemplate() );
        
        TaskFlowClient tmpTaskFlowClient = new TaskFlowClient( getAcceptedQuote().getSite() );
        tmpTaskFlowClient.setRestTemplate( taskFlowClient.getRestTemplate() );
        
        TaskClient tmpTaskClient = new TaskClient( getAcceptedQuote().getSite() );
        tmpTaskClient.setRestTemplate( taskClient.getRestTemplate() );

        if( !isRestartedTask || !getOrderBean().hasWorkflowId() ) {
            final ResponseEntity< Specifier< Facets > > responseEntity = tmpGorfxClient.createTaskFlow(
                    ProviderStageInMeta.PROVIDER_STAGING_KEY,
                    getOrderBean().createProviderStagInOrder(),
                    getOrder().getDNFromContext(),
                    getOrder().getActId(),
                    LanguageAlgebra.getMultiValueMapFromMap(getOrder().getActContext() ) );

            getOrderBean().setWorkflowId( responseEntity.getBody().getUriMap().get( "id" ) );
            
            tmpTaskFlowClient.setQuote(
                    ProviderStageInMeta.PROVIDER_STAGING_KEY,
                    responseEntity.getBody().getUriMap().get( UriFactory.TASKFLOW_ID ),
                    getAcceptedQuote(),
                    getOrder().getDNFromContext(),
                    getOrder().getActId() );
        }

        if( !isRestartedTask || !getOrderBean().hasTaskId() ) {
            final ResponseEntity<Specifier<Facets>> responseEntity = tmpTaskFlowClient.createTask(
                    ProviderStageInMeta.PROVIDER_STAGING_KEY,
                    getOrderBean().getWorkflowId(),
                    0,
                    getOrder().getDNFromContext(),
                    getOrder().getActId());
            
            getOrderBean().setTaskId( responseEntity.getBody().getUriMap().get( UriFactory.TASK_ID ) );
        }
                
        while( true ) {
            final ResponseEntity< TaskFlowStatus > status = tmpTaskFlowClient.getStatus(
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

                tmpTaskFlowClient.deleteTaskflow(
                        ProviderStageInMeta.PROVIDER_STAGING_KEY,
                        getOrderBean().getWorkflowId(),
                        getOrder().getDNFromContext(),
                        getOrder().getActId() );

                break;
            }

            MapConfig config = new MapConfig( getConfigMapData() );
            final Integer updateInterval = config.getIntOption( "updateInterval", 60000 );
            Thread.sleep( updateInterval );
        }
    }


    public Map< String, String > getConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType taskFlowType = session.findTaskFlowType( DmsStageInMeta.DMS_STAGE_IN_KEY );
            final Map< String, String > configMapData = taskFlowType.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
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
    public void setTaskClient( TaskClient taskClient ) {
        this.taskClient = taskClient;
    }

    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setGorfxClient( GORFXClient gorfxClient ) {
        this.gorfxClient = gorfxClient;
    }


    public Dao getDao( ) {
        return dao;
    }

    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setDao( Dao dao ) {
        this.dao = dao;
    }


    public Quote getAcceptedQuote() {
        return acceptedQuote;
    }


    public void setAcceptedQuote( Quote acceptedQuote ) {
        this.acceptedQuote = acceptedQuote;
    }
}
