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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transaction;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;

/**
 * The subspace provider which handles the available subspaces providing 
 * a mapping of subspace ids and subspaces.
 * 
 * @author Ulrike Golas
 */
public class SubspaceProviderImpl implements SubspaceProvider {

	/**
	 * The entity manager factory.
	 */
	private EntityManagerFactory emf;
	/**
	 * The entity manager.
	 */
	private EntityManager em;

	/**
	 * List of subspace ids.
	 */
    private Map<String, Subspace> subspaceIds;
    
    /**
     * The constructor initializing the entity manager and querying all subspaces.
     */
    @SuppressWarnings("unchecked")
	public SubspaceProviderImpl() {
    	em = emf.createEntityManager();
       	TxFrame tx = new TxFrame(em);
    	try {
       		Query query = em.createNamedQuery("listAllSubspaceIds");
       		List<ImmutableScopedName> list = query.getResultList();
       		subspaceIds = new HashMap<String, Subspace>();
       		for (ImmutableScopedName name : list) {
       			Subspace sub = em.find(Subspace.class, name);
       			subspaceIds.put(name.toString(), sub);
       		}
        tx.commit();
       	} finally {
       		tx.finish();
       		if (em.isOpen()) {
       			em.close();
       		}
       	}
    }
    
	@Override
	public final boolean exists(final String subspace) {
        return subspaceIds.containsKey(subspace);
	}

	@Override
	public final List<String> listSubspaces() {
        return new ArrayList<String>(subspaceIds.keySet());
	}

	@Override
	public final Subspace getSubspace(final String subspace) {
		ImmutableScopedName name = new ImmutableScopedName(subspace);
		return em.find(Subspace.class, name);
	}

	/**
	 * @return the emf
	 */
	public final EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * @param emf the emf to set
	 */
	@PersistenceUnit
	public final void setEmf(final EntityManagerFactory emf) {
		this.emf = emf;
	}

}
