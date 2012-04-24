package de.zib.gndms.logic.model.dspace;
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

import de.zib.gndms.logic.model.ModelTaskAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.12.11  17:11
 * @brief
 */
public class DeleteSliceKindTaskAction extends ModelTaskAction<ModelIdHoldingOrder> {

    private SliceKindProvider sliceKindProvider;


    public DeleteSliceKindTaskAction( ) {
        super( ModelIdHoldingOrder.class );
    }


    @Override
    protected void onCreated( @NotNull final String wid, @NotNull final TaskState state,
                              final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {
        ensureOrder();

        if (! isRestartedTask ) {
            SliceKind sliceKind;
            sliceKind = getModelEntity( SliceKind.class );

            EntityManager em = getEmf().createEntityManager();
            TxFrame tx = new TxFrame( em );
            List<Slice> slices;
            try {
                final Query listAllSlicesOfKind = em.createNamedQuery( "listAllSlicesOfKind" );
                listAllSlicesOfKind.setParameter( "sliceKindId", sliceKind.getId() );
                slices = listAllSlicesOfKind.getResultList();
                tx.commit();
            } finally {
                tx.finish();
            }

            updateMaxProgress( slices.size()+1 );
            updateProgress( 0 );
        }
        super.onCreated(wid, state, isRestartedTask, altTaskState);
    }


    @Override
    protected void onInProgress( @NotNull final String wid, @NotNull final TaskState state,
                                 final boolean isRestartedTask, final boolean altTaskState )
            throws Exception
    {
        ensureOrder();

        SliceKind sliceKind;
        sliceKind = getModelEntity( SliceKind.class );

        EntityManager em = getEmf().createEntityManager();
        TxFrame tx = new TxFrame( em );
        List<Slice> slices;
        try {
            final Query listAllSlicesOfKind = em.createNamedQuery( "listAllSlicesOfKind" );
            listAllSlicesOfKind.setParameter( "sliceKindId", sliceKind.getId() );
            slices = listAllSlicesOfKind.getResultList();
            tx.commit();
        } finally {
            tx.finish();
        }

        int progress = 0;
        updateMaxProgress( slices.size()+1 );
        updateProgress( progress );
        for( Slice slice : slices ) {
            getDirectoryAux().deleteDirectory( slice.getOwner(),
                    slice.getSubspace().getPathForSlice( slice ) );
            deleteEntity( slice.getClass(), slice.getId() );
            updateProgress( ++progress );
        }


        em = getEmf().createEntityManager();
        tx = new TxFrame( em );
        List<Subspace> subspaces;
        try {
            final Query listAllSlicesOfKind = em.createNamedQuery( "getSubspacesUsingSliceKind" );
            listAllSlicesOfKind.setParameter( "idParam", sliceKind.getId() );
            subspaces = listAllSlicesOfKind.getResultList();
            tx.commit();
        } finally {
            tx.finish();
        }

        for ( Subspace  subspace : subspaces  )
            deleteFromSubspace( sliceKind, subspace );

        deleteModelEntity( SliceKind.class );
        sliceKindProvider.invalidate( sliceKind.getId() );

        updateProgress( ++progress );

        autoTransitWithPayload( Boolean.TRUE );
    }


    private void deleteFromSubspace( final SliceKind sliceKind, final Subspace subspace ) {

        try {
            getDirectoryAux().deleteDirectory( System.getenv( "uid" ),
                    subspace.getPath() + File.separator + sliceKind.getSliceDirectory() );
        }
        catch( Exception e ) {
            logger.debug( e.getMessage() );
        }

        EntityManager em = getEmf().createEntityManager();
        TxFrame tx = new TxFrame( em );
        try {
            Subspace attachedSubspace = em.find( Subspace.class, subspace.getId() );
            attachedSubspace.getCreatableSliceKinds().remove( sliceKind );
            tx.commit();
        } finally {
            tx.finish();
        }
    }


    @Inject
    public void setSliceKindProvider( SliceKindProvider sliceKindProvider ) {
        this.sliceKindProvider = sliceKindProvider;
    }
}
