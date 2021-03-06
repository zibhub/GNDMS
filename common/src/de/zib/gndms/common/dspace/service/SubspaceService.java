package de.zib.gndms.common.dspace.service;

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

/**
 * The interface of the Subspace service.
 * 
 * Clients and resources which should provide the Subpace service should
 * implement this interface.
 * 
 * Some general remarks on the parameters:
 * <ul>
 * <li>The <b>dn</b> parameter is a identifier for the user responsible for the
 * method invocation. It is required to check if the user has the necessary
 * permissions for the call. <br>
 * Using REST the attribute should be provided using a http-header property
 * called "DN".
 * </ul>
 * 
 * @author Ulrike Golas
 */
public interface SubspaceService {

	/**
	 * Lists all facets of the subspace.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The available facets.
	 */
	ResponseEntity<Facets> listAvailableFacets(String subspace, String dn);

	/**
	 * Creates a subspace with the given configuration.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param config
	 *            The subspace configuration.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The facets of the created subspace.
	 */
	ResponseEntity<Facets> createSubspace(String subspace, String config, String dn);

	/**
	 * Deletes a subspace.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity< Specifier< Facets > > deleteSubspace(String subspace, String dn);

	/**
	 * Lists all available slice kinds.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The list of slice kinds.
	 */
	ResponseEntity<List<Specifier<Void>>> listSliceKinds(String subspace,
			String dn);

    /**
     * Create a slice kind.
     *
     * @param subspace
     *            The subspace identifier.
     * @param sliceKind
     *            The sliceKind identifier.
     * @param dn
     *            The dn of the user invoking the method.
     * @return The list of slice kinds.
     */
    ResponseEntity< Specifier< Void > > createSliceKind(String subspace,
            String sliceKind, String config, String dn);

	/**
	 * Lists a subspace configuration.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The subspace information (including configuration).
	 */
	ResponseEntity< SubspaceInformation > getSubspaceInformation( String subspace, String dn );

	/**
	 * Changes a subspace configuration.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param config
	 *            The subspace configuration.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return ??.
	 */
	ResponseEntity<Void> setSubspaceConfiguration(String subspace,
			Configuration config, String dn);

    ResponseEntity< Specifier< Void > > createSlice(
            String subspace, String sliceKind,
            String config, String dn );
}
