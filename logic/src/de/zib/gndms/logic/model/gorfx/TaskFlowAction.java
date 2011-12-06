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
import de.zib.gndms.common.rest.MyProxyToken;
import de.zib.gndms.kit.access.MyProxyFactory;
import de.zib.gndms.kit.access.MyProxyFactoryProvider;
import de.zib.gndms.kit.security.CredentialProvider;
import de.zib.gndms.kit.security.MyProxyCredentialProvider;
import de.zib.gndms.logic.model.DefaultTaskAction;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.neomodel.gorfx.Task;
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
public abstract class TaskFlowAction<K extends AbstractOrder> extends DefaultTaskAction {

    private String offerTypeId;
    private DelegatingOrder<K> order;
    private MyProxyFactoryProvider myProxyFactoryProvider;


    protected TaskFlowAction( String offerTypeId ) {
        super();
        this.offerTypeId = offerTypeId;
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
                final TaskFlowType ot = session.findTaskFlowType( offerTypeId );
                final Task task = getModel().getTask(session);
                task.setTaskFlowType( ot );
                task.setWID(wid);
                setOrder( (DelegatingOrder) task.getOrder( ) );
                session.success();
            }
            finally { session.finish(); }
        }
    }


    public String getOfferTypeId() {
        return offerTypeId;
    }


    public Map<String, String> getOfferTypeConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            // todo add tft to database ...
            final TaskFlowType ot = session.findTaskFlowType( getOfferTypeId() );
            final Map<String,String> configMapData = ot.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }


    public void setOfferTypeId(@NotNull final String keyParam) {
        offerTypeId = keyParam;
    }


    public String getKey() {
        return offerTypeId;
    }

    public void setKey(@NotNull String keyParam) {
        setOfferTypeId(keyParam);
    }

    public K getOrderBean() {
        if( order != null )
            return order.getOrderBean();

        return null;
    }


    // returns cached instance of order
    public DelegatingOrder<K> getOrder() {
        return order;
    }


    protected void setOrder( DelegatingOrder<K> order ) {
        this.order = order;
    }


    public abstract Class<K> getOrderBeanClass( );

    protected void failFrom( Exception e ) {
    	new IllegalStateException( getFailString( e ), e );
    }

    protected void traceFrom( Exception e ) {
        trace( getFailString( e ), e );
    }


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


    protected CredentialProvider getCredentialProviderFor( final String requiredCredentialName ) {

        final Map<String, MyProxyToken> myProxyToken = getOrder().getMyProxyToken();
        MyProxyToken token;
        if ( myProxyToken.containsKey( requiredCredentialName ) )
            token = myProxyToken.get( requiredCredentialName );
        else
            throw new IllegalStateException( "no security token for: " + requiredCredentialName );

        MyProxyFactory myProxyFactory = getMyProxyFactoryProvider().getFactory( requiredCredentialName );
        if( myProxyFactory == null )
            throw new IllegalStateException( "no MyProxy-Server registered for "  +
                    requiredCredentialName );

        return new MyProxyCredentialProvider( myProxyFactory, token.getLogin(),
                token.getPassword() );
    }


    /**
     * This should be called in every In_State_ method, cause after a restart order might have
     * not been initialized.
     */
    protected void ensureOrder() {

        if ( getOrder() == null ) {
            Session session = getDao().beginSession();
            try {
                Task task = getTask( session );
                setOrder( ( DelegatingOrder ) task.getOrder() );
                session.success();
            } finally {
                session.finish();
            }
        }
    }
}
