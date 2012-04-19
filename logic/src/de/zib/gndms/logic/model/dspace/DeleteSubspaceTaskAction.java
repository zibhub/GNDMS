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

package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.model.ModelTaskAction;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Future;

/**
 * @date: 19.04.12
 * @time: 09:47
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DeleteSubspaceTaskAction extends ModelTaskAction<ModelIdHoldingOrder> {

    public DeleteSubspaceTaskAction() {
        super( ModelIdHoldingOrder.class );
    }


    @Override
    protected void onCreated( @NotNull final String wid, @NotNull final TaskState state,
                              final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {
        if (! isRestartedTask ) {
            final Subspace subspace = getModelEntity( Subspace.class );
            final Session session = getDao().beginSession();
            try {
                final Task task = getModel().getTask( session );
                task.setWID( wid );
                task.setMaxProgress( subspace.getCreatableSliceKinds().size() );
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
        ensureOrder();

        final Subspace subspace = getModelEntity( Subspace.class );

        if( null == subspace )
            throw new IllegalArgumentException( "Could not find subspace " + getOrder().getModelId() );

        final Collection< Future< Taskling > > futures = new HashSet< Future< Taskling > >();

        // submit tasks to delete all slicekinds
        {
            final Session session = getDao().beginSession();
            try {
                final Task task = getTask( session );

                for( SliceKind sk: subspace.getCreatableSliceKinds() ) {
                    final ModelIdHoldingOrder order = new ModelIdHoldingOrder( sk.getId() );
                    final Task subtask = task.createSubTask();
                    subtask.setId( getUUIDGen().nextUUID() );
                    subtask.setTerminationTime( task.getTerminationTime() );
                    subtask.setOrder( order );

                    DeleteSliceKindTaskAction deleteSliceKindTaskAction
                            = new DeleteSliceKindTaskAction();
                    deleteSliceKindTaskAction.setOwnDao(getDao());
                    deleteSliceKindTaskAction.setEmf( getEmf() );

                    final Future< Taskling > future = this.getService().submitAction( deleteSliceKindTaskAction );
                    futures.add( future );

                    session.success();
                }
            }
            finally { session.finish(); }
        }
        
        // wait for all tasks to finish
        {
            boolean finished;

            do {
                finished = true;

                int maxProgress = 0;
                int progress = 0;

                final Session session = getDao().beginSession();
                try {
                    final Task task = getTask( session );

                    for( Task subtask: task.getSubTasks() ) {
                        maxProgress += subtask.getMaxProgress();
                        progress += subtask.getProgress();

                        if( !subtask.isDone() )
                            finished = false;
                        else if( subtask.getTaskState().equals( TaskState.FAILED ) ) {
                            transit( TaskState.FAILED );
                            failWithPayload( null, subtask.getCause().toArray( new Exception[]{ } ) );
                        }
                    }

                    task.setMaxProgress( maxProgress );
                    task.setProgress( progress );

                    session.success();
                }
                finally { session.finish(); }

                if( !finished )
                    Thread.sleep( 1000 );
            } while( !finished );
        }

        deleteModelEntity( Subspace.class );

        autoTransitWithPayload( Boolean.TRUE );
    }
}
