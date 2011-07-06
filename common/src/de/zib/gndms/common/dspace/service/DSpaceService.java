package de.zib.gndms.common.dspace.service;

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

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.zib.gndms.common.rest.Specifier;

/**
 * The interface of the DSpace service.
 * 
 * Clients and resources which should provide the DSpace service should
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
public interface DSpaceService {

	/**
	 * Lists all subspaces of the dspace.
	 * 
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return List of subspaces.
	 */
	ResponseEntity<List<Specifier<Void>>> listSubspaceSpecifiers(String dn);

}
