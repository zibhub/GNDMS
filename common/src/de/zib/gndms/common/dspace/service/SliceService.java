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

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * The interface of the Slice service.
 * 
 * Clients and resources which should provide the Slice service should
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
public interface SliceService {

	/**
	 * Lists the facet of a slice.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The slice configuration and the available facets.
	 */
	ResponseEntity< Facets > listSliceFacets(String subspace, String sliceKind,
			String slice, String dn);

	/**
	 * Updates a slice with a new configuration.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param config The configuration of the slice.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> setSliceConfiguration(String subspace, String sliceKind,
			String slice, Configuration config, String dn);

	/**
	 * Changes the slice kind of a slice.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param newSliceKind
	 *            The specifier of the target slice kind for the slice.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The slice specifier.
	 */
	ResponseEntity<Specifier<Void>> transformSlice(String subspace, String sliceKind,
			String slice, Specifier<Void> newSliceKind, String dn);

	/**
	 * Deletes a whole slice.
	 * 
	 *
     * @param subspace
     *            The subspace identifier.
     * @param sliceKind
     *            The slice kind identifier.
     * @param slice
     *            The slice identifier.
     * @param dn
     *            The dn of the user invoking the method.
     * @return A task specifier.
	 */
	ResponseEntity<Specifier<Facets>> deleteSlice(String subspace, String sliceKind,
                                                  String slice, String dn);

	/**
	 * Lists all files of a slice fulfilling the specified attributes.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param attr
	 *            The requested attributes.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The list of files.
	 */
	ResponseEntity<List<File>> listFiles(String subspace, String sliceKind,
			String slice, Map<String, String> attr, String dn);

	/**
	 * Deletes all files of a slice.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> deleteFiles(String subspace, String sliceKind,
			String slice, String dn);

	/**
	 * Returns the URL of the slice for GridFTP access.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The URL.
	 */
	ResponseEntity<String> getGridFtpUrl(String subspace, String sliceKind,
			String slice, String dn);

	/**
	 * Selects a specific file.
	 * 
	 *
     * @param subspace
     *            The subspace identifier.
     * @param sliceKind
     *            The slice kind identifier.
     * @param slice
     *            The slice identifier.
     * @param fileName
     *            The file name.
     * @param attrs
     * 			  The attributed to be shown.
     * @param dn
     *            The dn of the user invoking the method.
     * @param out
     * 			  The outputstream the file information shall be written to.
     * @return The file.
	 */
	// TODO: GNDMSFile ?
	ResponseEntity<void> listFileContent(String subspace, String sliceKind,
                                         String slice, String fileName, List<String> attrs, String dn, OutputStream out);

	/**
	 * Selects a specific file.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param fileName
	 *            The file name.
	 * @param file
	 *            The content of the file.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> setFileContent(String subspace, String sliceKind,
			String slice, String fileName, MultipartFile file, String dn);

	/**
	 * Deletes a specific file.
	 * 
	 * @param subspace
	 *            The subspace identifier.
	 * @param sliceKind
	 *            The slice kind identifier.
	 * @param slice
	 *            The slice identifier.
	 * @param fileName
	 *            The file name.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> deleteFile(String subspace, String sliceKind,
			String slice, String fileName, String dn);

}
