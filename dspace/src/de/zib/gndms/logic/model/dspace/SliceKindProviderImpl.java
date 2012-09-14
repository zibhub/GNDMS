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

import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.infra.grams.LinuxDirectoryAux;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.util.GridResourceCache;
import de.zib.gndms.neomodel.gorfx.Taskling;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

/**
 * The slice kind provider which handles the available subspaces providing 
 * a mapping of slice kind ids and slice kinds.
 * 
 * @author Jörg Bachmann
 */
public class SliceKindProviderImpl extends GridResourceDAO< SliceKind > implements SliceKindProvider {

    private GNDMSystem system;

    public SliceKindProviderImpl( final EntityManagerFactory emf ) {
        super(
                emf,
                new GridResourceCache< SliceKind >(
                        SliceKind.class,
                        emf
                ),
                SetupSliceKindAction.class
        );
    }


    protected String getListQuery( ) {
        return "listSlicekindsOfSubspace";
    }


    @Override
    public boolean exists( String subspace, String sliceKind ) {
        return super.exists( sliceKind );
    }


    @Override
    public List< String > list( String subspace ) throws NoSuchElementException {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( getListQuery() );
        query.setParameter( "idParam", subspace );
        return query.getResultList();
    }

    @Override
    public SliceKind get( String subspace, String sliceKind ) throws NoSuchElementException {
        return super.get( sliceKind );
    }


    public void create( final String sliceKindId, final String config ) {
        try {
            final StringWriter sw = new StringWriter();

            final SetupSliceKindAction setup_action = new SetupSliceKindAction();
            setup_action.setPrintWriter( new PrintWriter( sw ) );
            setup_action.parseLocalOptions( "sliceKind:" + sliceKindId + "; " + config );
            setup_action.setMode( SetupMode.CREATE );
            actionConfigurer.configureAction( setup_action );

            final AssignSliceKindAction assign_action = new AssignSliceKindAction();
            assign_action.setPrintWriter( new PrintWriter( sw ) );
            assign_action.parseLocalOptions( "sliceKind:" + sliceKindId + "; " + config );
            actionConfigurer.configureAction( assign_action );

            // TODO: handle assign action as postponed action of setup_action
            ConfigActionResult result = setup_action.call();
            assign_action.call();

            logger.info( sw.toString() );
        }
        catch( ParameterTools.ParameterParseException e ) {
            throw new IllegalStateException( "Error on parsing parameter string '" + config + "'.", e );
        }
    }
    
    
    public Taskling delete( final String sliceKindId ) {
        // Removing the subspace assignees is done in the DeleteSliceKindTaskAction, although not very nice...
        /*
        try {
            final StringWriter sw = new StringWriter();

            final AssignSliceKindAction assign_action = new AssignSliceKindAction();
            assign_action.setPrintWriter( new PrintWriter( sw ) );
            assign_action.parseLocalOptions( "sliceKind:" + sliceKindId );
            actionConfigurer.configureAction( assign_action );

            // TODO: handle assign action as postponed action of setup_action
            assign_action.call();

            logger.info( sw.toString() );
        }
        catch( ParameterTools.ParameterParseException e ) {
            throw new IllegalStateException( "Error on parsing parameter string '" + "sliceKind:" + sliceKindId + "'.", e );
        }
        */


        final DeleteSliceKindTaskAction deleteAction = new DeleteSliceKindTaskAction();
        deleteAction.setDirectoryAux( new LinuxDirectoryAux() );
        system.getInstanceDir().getSystemAccessInjector().injectMembers( deleteAction );
        deleteAction.setEmf( emf );
        //actionConfigurer.configureAction(deleteAction);

        final Order order = new ModelIdHoldingOrder( sliceKindId );
        final String wid = UUID.randomUUID().toString();
        logger.info( "Delete sliceKindID (" + sliceKindId + ") with tracking number " + wid );
        final Taskling ling = system.submitTaskAction( deleteAction, order, wid );

        invalidate( sliceKindId );

        return ling;
    }
    
    
    public void invalidate( final String sliceKindId ) {
        invalidateCacheFor( sliceKindId );
    }


    @Inject
    public void setSystem(GNDMSystem system) {
        this.system = system;
    }
}
