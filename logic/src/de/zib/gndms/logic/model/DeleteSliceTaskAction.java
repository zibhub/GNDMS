package de.zib.gndms.logic.model;
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

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 20.12.11  11:44
 * @brief
 */
public class DeleteSliceTaskAction extends DefaultTaskAction {

    private DirectoryAux directoryAux;


    public DeleteSliceTaskAction() {

        super();
    }


    public DeleteSliceTaskAction( @NotNull EntityManager em, @NotNull Dao dao,
                                  @NotNull Taskling model )
    {
        super( em, dao, model );
    }


    @Override
    protected void onCreated( @NotNull final String wid, @NotNull final TaskState state,
                              final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {

        if (! isRestartedTask) {
            super.onCreated(wid, state, isRestartedTask, altTaskState);
            final Session session = getDao().beginSession();
            try {
                final Task task = getModel().getTask(session);
                task.setWID(wid);
                task.setMaxProgress( 1 );
                task.setProgress( 0 );
                session.success();
            }
            finally { session.finish(); }
        }
        super.onCreated( wid, state, isRestartedTask, altTaskState );
    }


    @Override
    protected void onInProgress( @NotNull final String wid, @NotNull final TaskState state,
                                 final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {
        Session session = getDao().beginSession();
        ModelIdHoldingOrder order;
        try{      
            final Task task = getModel().getTask(session);
            order = ( ModelIdHoldingOrder ) task.getOrder();
            session.success();
        } finally { session.finish(); }
        
        EntityManager em = getEmf().createEntityManager();
        TxFrame tx = new TxFrame( em );
        Slice slice;
        Subspace subspace;
        try{
            slice = em.find( Slice.class, order.getModelId() );
            subspace = slice.getSubspace();
            tx.commit();
        } finally {
            tx.finish();
        }


        getDirectoryAux().deleteDirectory( slice.getOwner(), subspace.getPathForSlice( slice ) );

        em = getEmf().createEntityManager();
        tx = new TxFrame( em );
        try{
            slice = em.find( Slice.class, order.getModelId() );
            getEntityManager().remove( slice );
            tx.commit();
        } finally {
            tx.finish();
        }

        autoTransitWithPayload( Boolean.TRUE );
    }


    public DirectoryAux getDirectoryAux() {

        return directoryAux;
    }


    @Inject
    public void setDirectoryAux( final DirectoryAux directoryAux ) {

        this.directoryAux = directoryAux;
    }
}