package de.zib.gndms.common.dspace.service;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * The interface of the Slice service clients.
 * 
 * Clients and resources which should provide the Slice service should
 * implement this interface. Appart from the methods from the
 * SliceService itself this interface provides some methods which
 * makes it easier to use this client with the specifier types.
 * 
 * Some general remarks on the parameters:
 * <ul>
 * <li>The <b>dn</b> parameter is a identifier for the user responsible for the
 * method invocation. It is required to check if the user has the necessary
 * permissions for the call. <br>
 * Using REST the attribute should be provided using a http-header property
 * called "DN".
 * <li> Specifiers as in parameter should always contain an url
 * identifying the resource.
 * </ul>
 * 
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 16.02.12  11:54
 */
public interface SliceServiceClient extends SliceService {


	/**
	 * Lists the facet of a slice.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The slice configuration and the available facets.
	 */
	ResponseEntity< Facets > listSliceFacets( Specifier<Void> slice, String dn);

	/**
	 * Updates a slice with a new configuration.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param config The configuration of the slice.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> setSliceConfiguration( Specifier<Void> slice, Configuration config, String dn);

	/**
	 * Changes the slice kind of a slice.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param newSliceKind
	 *            The specifier of the target slice kind for the slice.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The slice specifier.
	 */
	ResponseEntity<Specifier<Void>> transformSlice( Specifier<Void> slice, Specifier<Void> newSliceKind, String dn);

	/**
	 * Deletes a whole slice.
	 * 
	 * @param slice
	 *            The slice specifier.
     * @param dn
     *            The dn of the user invoking the method.
     * @return A task specifier.
	 */
	ResponseEntity<Specifier<Facets>> deleteSlice( Specifier<Void> slice, String dn);

	/**
	 * Lists all files of a slice fulfilling the specified attributes.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param attr
	 *            The requested attributes.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The list of files.
	 */
	ResponseEntity< List<FileStats> > listFiles( Specifier<Void> slice, String dn);

	/**
	 * Deletes all files of a slice.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> deleteFiles( Specifier<Void> slice, String dn);

	/**
	 * Returns the URL of the slice for GridFTP access.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return The URL.
	 */
	ResponseEntity<String> getGridFtpUrl( Specifier<Void> slice, String dn);

	/**
	 * Selects a specific file.
	 * 
	 *
     * @param slice
     *            The slice specifier.
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
	ResponseEntity<Void> listFileContent( Specifier<Void> slice, String fileName, List<String> attrs, String dn, OutputStream out);

	/**
	 * Selects a specific file.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param fileName
	 *            The file name.
	 * @param file
	 *            The content of the file.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> setFileContent( Specifier<Void> slice, String fileName, MultipartFile file, String dn);

	/**
	 * Deletes a specific file.
	 * 
	 * @param slice
	 *            The slice specifier.
	 * @param fileName
	 *            The file name.
	 * @param dn
	 *            The dn of the user invoking the method.
	 * @return A confirmation.
	 */
	ResponseEntity<Void> deleteFile( Specifier<Void> slice, String fileName, String dn);

}
