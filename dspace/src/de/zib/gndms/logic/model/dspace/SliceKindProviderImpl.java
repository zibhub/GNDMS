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

import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.util.GridResourceCache;

import javax.persistence.EntityManagerFactory;
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
                SetupSubspaceAction.class
        );
    }

    protected String getListQuery( ) {
        return "listCreatableSliceKinds";
    }

    @Override
    public boolean exists( String subspace, String sliceKind ) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SliceKind> list( String subspace ) throws NoSuchElementException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SliceKind get( String subspace, String sliceKind ) throws NoSuchElementException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
