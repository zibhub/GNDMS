package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.kit.access.MyProxyFactoryProvider;
import de.zib.gndms.kit.security.GNDMSSecurityContextInstaller;
import de.zib.gndms.logic.model.DefaultTaskAction;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Map;


/**
 * Adds some type-safety to TaskActions
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 13:00:56
 */
public abstract class TaskFlowAction<K extends AbstractOrder> extends
        DefaultTaskAction<DelegatingOrder<K>> {

    private String taskFlowTypeId;
    private MyProxyFactoryProvider myProxyFactoryProvider;
    private GNDMSSecurityContextInstaller securityContextInstaller;


    protected TaskFlowAction( String taskFlowTypeId ) {
        super( ( Class<DelegatingOrder<K>>) (Object) DelegatingOrder.class );
        this.taskFlowTypeId = taskFlowTypeId;
    }


    public TaskFlowAction( @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model ) {
        super(em, dao, model);
    }


    @Override
    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        if (! isRestartedTask) {
            super.onCreated(wid, state, isRestartedTask, altTaskState);
            final Session session = getDao().beginSession();
            try {
                final TaskFlowType ot = session.findTaskFlowType( taskFlowTypeId );
                final Task task = getModel().getTask(session);
                task.setTaskFlowType( ot );
                task.setWID(wid);
                setOrder( (DelegatingOrder) task.getOrder( ) );
                restoreSecurityContext( );
                session.success();
            }
            finally { session.finish(); }
        }
    }


    protected void restoreSecurityContext() {
        ensureOrder();
        securityContextInstaller.installSecurityContext( getOrder().getSecurityContextHolder() );
    }


    public String getTaskFlowTypeId() {
        return taskFlowTypeId;
    }


    public Map<String, String> getTaskFlowTypeConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType ot = session.findTaskFlowType( getTaskFlowTypeId() );
            final Map<String,String> configMapData = ot.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }


    public void setTaskFlowTypeId( @NotNull final String keyParam ) {
        taskFlowTypeId = keyParam;
    }


    public String getKey() {
        return taskFlowTypeId;
    }

    public void setKey(@NotNull String keyParam) {
        setTaskFlowTypeId( keyParam );
    }

    public K getOrderBean() {
        if( getOrder() != null )
            return getOrder().getOrderBean();

        return null;
    }


    public abstract Class<K> getOrderBeanClass( );


    protected String getFailString( Exception e )  {
        if( e != null )
            // todo verify getTaskSnapshot doesn't cause trouble on error
            return getTaskSnapshot(getDao()).getDescription() + " failure " +  e.getMessage();
        else
            return getTaskSnapshot(getDao()).getDescription() + " failure (no Exception provided)";
    }


    public MyProxyFactoryProvider getMyProxyFactoryProvider() {

        return myProxyFactoryProvider;
    }


    @Inject
    public void setMyProxyFactoryProvider( final MyProxyFactoryProvider myProxyFactoryProvider ) {

        this.myProxyFactoryProvider = myProxyFactoryProvider;

    }


    public GNDMSSecurityContextInstaller getSecurityContextInstaller() {

        return securityContextInstaller;
    }


    @Inject
    public void setSecurityContextInstaller( final GNDMSSecurityContextInstaller securityContextInstaller ) {
        this.securityContextInstaller = securityContextInstaller;
    }
}
