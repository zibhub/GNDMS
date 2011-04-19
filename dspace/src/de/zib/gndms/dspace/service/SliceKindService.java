package de.zib.gndms.dspace.service;

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

import org.springframework.http.ResponseEntity;

import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.rest.Specifier;
import de.zib.gndms.stuff.confuror.ConfigHolder;

/**
 * The interface of the SliceKind service.
 * 
 * Clients and resources which should provide the SliceKind service should
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
public interface SliceKindService {

	/**
	 * Lists the slice kind representation.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The representation of the slice kind.
	 */
	ResponseEntity<ConfigHolder> getSliceKindInfo(String subspace,
			String sliceKind, String dn);

	/**
	 * Sets the configuration of the slice kind.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param config The configuration of the slice kind.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The representation of the slice kind.
	 */
	ResponseEntity<Specifier<SliceKind>> setSliceKindConfig(String subspace,
			String sliceKind, ConfigHolder config, String dn);

	/**
	 * Deletes a slice kind.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param config The configuration of the slice kind.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> createSliceKind(String subspace, String sliceKind,
			ConfigHolder config, String dn);

	/**
	 * Deletes a slice kind.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> deleteSliceKind(String subspace, String sliceKind,
			String dn);

}
