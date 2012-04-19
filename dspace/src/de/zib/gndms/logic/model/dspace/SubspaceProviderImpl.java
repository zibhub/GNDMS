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

package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.util.GridResourceCache;
import de.zib.gndms.neomodel.gorfx.Taskling;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

public class SubspaceProviderImpl extends GridResourceDAO< Subspace > implements SubspaceProvider {

    private GNDMSystem system;

	public SubspaceProviderImpl( final EntityManagerFactory emf ) {
        super(
                emf,
                new GridResourceCache< Subspace >(
                        Subspace.class,
                        emf
                ),
                SetupSubspaceAction.class
        );
    }

    protected String getListQuery( ) {
        return "listAllSubspaceIds";
    }

    @Override
    public boolean exists( String subspace ) {
        return super.exists( subspace );
    }

    @Override
    public List< String > list() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( getListQuery() );
        return query.getResultList();
    }
    
    @Override
    public Taskling delete(final String subspaceId) throws NoSuchElementException {
        if( !exists( subspaceId ) ) {
            logger.info( "Illegal Access: subspace " + subspaceId + " cannot be deleted because it is not available." );
            throw new NoSuchElementException( "Subspace " + subspaceId + " does not exist." );
        }
        final DeleteSubspaceTaskAction deleteAction = new DeleteSubspaceTaskAction();
        system.getInstanceDir().getSystemAccessInjector().injectMembers( deleteAction );

        final Order order = new ModelIdHoldingOrder( subspaceId );
        final String wid = UUID.randomUUID().toString();
        logger.info( "Delete subspaceID (" + subspaceId + ") with tracking number " + wid );
        final Taskling ling = system.submitTaskAction( deleteAction, order, wid );

        invalidateCacheFor( subspaceId );

        return ling;
    }


    @Inject
    public void setSystem(GNDMSystem system) {
        this.system = system;
    }

}
