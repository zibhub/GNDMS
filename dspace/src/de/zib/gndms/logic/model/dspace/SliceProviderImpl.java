package de.zib.gndms.logic.model.dspace;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;

/**
 * The slice provider which handles the available subspaces providing 
 * a mapping of slice ids and slices.
 * 
 * @author Ulrike Golas
 */

public class SliceProviderImpl implements SliceProvider {
	private EntityManagerFactory emf;
	private EntityManager em;
    private Map<String, Map<String, Slice>> slices;

    @Inject
    public void setProvider(SubspaceProvider provider) {
        this.provider = provider;
    }

    private SubspaceProvider provider;

    @SuppressWarnings("unchecked")
	@Override
	public final void init( ) {
        em = emf.createEntityManager();
       	TxFrame tx = new TxFrame(em);
    	try {
    		for (Subspace sub : provider.list()) {
				Map<String, Slice> map = new HashMap<String, Slice>();
           		Query query = em.createNamedQuery( "listSlicesOfSubspace" );
                query.setParameter("subspace", sub);
           		List<String> list = query.getResultList();
           		slices = new HashMap<String, Map<String, Slice>>();
           		for (String name : list) {
           			Slice slice = em.find(Slice.class, name);
           			map.put(name, slice);
           		}
				slices.put( sub.getId(), map );
			}
        tx.commit();
       	} finally {
       		tx.finish();
       		if (em != null && em.isOpen()) {
       			em.close();
       		}
       	}
	}

	@Override
	public final boolean exists(final String subspace, final String slice) {
		try {
			return slices.get(subspace).containsKey(slice);
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public final List<String> listSlices(final String subspace) throws NoSuchElementException {
		try {
	        return new ArrayList<String>(slices.get(subspace).keySet());
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Override
	public final Slice getSlice(final String subspace, final String slice) throws NoSuchElementException {
		try {
			return slices.get(subspace).get(slice);
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

}
