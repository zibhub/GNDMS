package de.zib.gndms.taskflows.publishing.server;


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


import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.publishing.client.PublishingTaskFlowMeta;
import de.zib.gndms.taskflows.publishing.client.model.PublishingOrder;
import de.zib.gndms.taskflows.publishing.client.model.PublishingTaskFlowResult;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author bachmann@zib.de
 * @date 31.07.2012  14:37
 * @brief The publishing action class
 *
 * @see PublishingOrder
 */
public class PublishingTFAction extends TaskFlowAction< PublishingOrder > {
    
    private final TransformerFactory transformerFactory;

    @Override
    public Class< PublishingOrder > getOrderBeanClass( ) {
        return PublishingOrder.class;
    }


    public PublishingTFAction() {
        super(PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY);

        transformerFactory = TransformerFactory.newInstance();
    }


    public PublishingTFAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY, em, dao, model);
        
        transformerFactory = TransformerFactory.newInstance();
    }


    @Override
    protected void onCreated( @NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState ) throws Exception {

        ensureOrder();

        // set max progress to number of files to download
        if( !isRestartedTask ) {
            PublishingOrder order = getOrderBean();

            final Session session = getDao().beginSession();
            try {
                Task task = getTask( session );
                task.setProgress(0);
                task.setMaxProgress( 1 );
                session.success();
            }
            finally { session.finish(); }
        }

        super.onCreated(wid, state, isRestartedTask, altTaskState);
    }


    @Override
    protected void onInProgress( @NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState ) throws Exception {
        final Slice slice = findSlice();

        if( null == slice ) {
            throw new IllegalArgumentException( "No Slice set in Order!" );
        }

        final String slicePath = slice.getSubspace().getPathForSlice( slice );
        final String oldMetaFile = slicePath + File.separatorChar + PublishingTaskFlowMeta.META_FILE;
        final String newMetaFile = slicePath + ".xml";

        // transform meta file to output
        try {
            Source xsltSource = new StreamSource( new FileInputStream( PublishingTaskFlowMeta.XSLT_FILE ) );
            Transformer transformer = transformerFactory.newTransformer( xsltSource );
            
            transformer.transform(
                    new StreamSource( new FileInputStream( oldMetaFile ) ),
                    new StreamResult( new FileOutputStream( newMetaFile ) )
            );
        }
        catch( RuntimeException e ) {
            transit( TaskState.FAILED );
        }

        setProgress( 1 );
        transitWithPayload( new PublishingTaskFlowResult(), TaskState.FINISHED );

        super.onInProgress(wid, state, isRestartedTask, altTaskState);    // overridden method implementation
    }


    private Slice findSlice() {
        final String sliceId = getOrderBean().getSliceId();

        getLogger().info( "findSlice(" + ( sliceId == null ? "null" : '"' + sliceId + '"' ) + ')' );
        if (sliceId == null)
            return null;


        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            final Slice slice = em.find(Slice.class, sliceId);
            txf.commit();
            return slice;
        }
        finally { txf.finish();  }
    }


    private void setProgress( int progress ) {
        final Session session = getDao().beginSession();
        try {
            Task task = getTask( session );
            task.setProgress( progress );
            session.success();
        }
        finally { session.finish(); }
    }


    @Override
    public DelegatingOrder< PublishingOrder > getOrder() {

        DelegatingOrder< PublishingOrder > order = null;

        final Session session = getDao().beginSession();
        try {
            order = ( DelegatingOrder< PublishingOrder > ) getTask( session ).getOrder( );
            session.success();
        }
        finally { session.finish(); }

        return order;
    }
}
