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
import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.util.GridResourceCache;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * The slice kind provider which handles the available subspaces providing 
 * a mapping of slice kind ids and slice kinds.
 * 
 * @author Jörg Bachmann
 */
public class SliceKindProviderImpl extends GridResourceDAO< SliceKind > implements SliceKindProvider {

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
    
    
    public void invalidate( final String sliceKindId ) {
        invalidateCacheFor( sliceKindId );
    }
}
