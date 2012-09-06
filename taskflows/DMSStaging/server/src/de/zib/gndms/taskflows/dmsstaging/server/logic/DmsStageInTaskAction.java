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

import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.CertificatePurpose;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.*;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInMeta;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInResult;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInResult;
import org.jetbrains.annotations.NotNull;

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
    private TaskFlowClient taskFlowClient;
    private TaskClient taskClient;
    private GORFXClient gorfxClient;

    private ProviderStageInResult result;
    
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

        AbstractTaskFlowExecClient etcf = new ExampleTaskFlowExecClient() {
            @Override
            public void handleResult( TaskResult res ) {

                result = ProviderStageInResult.class.cast( res );
            }


            @Override
            protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

                context.addMyProxyToken(
                        CertificatePurpose.C3GRID.toString(),
                        getOrder().getMyProxyToken().get( CertificatePurpose.C3GRID.toString().toLowerCase() )
                );
                return context;
            }
        };

        etcf.setGorfxClient( tmpGorfxClient );
        etcf.setTaskClient( tmpTaskClient );
        etcf.setTfClient( tmpTaskFlowClient );

        MapConfig config = new MapConfig( getConfigMapData() );
        final Integer updateInterval = config.getIntOption( "pollingInterval", 3000 );
        etcf.setPollingDelay( updateInterval );
        
        etcf.execTF(
                getOrderBean().createProviderStagInOrder(),
                getOrder().getDNFromContext(),
                true,
                getAcceptedQuote(),
                getOrder().getActId() );
        
        // ensure correct base URL
        result.getSliceSpecifier().getUriMap().put( UriFactory.BASE_URL, getAcceptedQuote().getSite() );

        transitWithPayload(new DmsStageInResult(result.getSliceSpecifier()), TaskState.FINISHED);
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


    public Quote getAcceptedQuote() {
        return acceptedQuote;
    }


    public void setAcceptedQuote( Quote acceptedQuote ) {
        this.acceptedQuote = acceptedQuote;
    }
}
