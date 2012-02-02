package de.zib.gndms.gndmc.dspace;

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

import de.zib.gndms.common.dspace.service.SliceService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * The slice client implementing the slice service.
 * 
 * @author Ulrike Golas
 */
public class SliceClient extends AbstractClient implements SliceService {

	/**
	 * The constructor.
	 */
	public SliceClient() {
	}

    /**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public SliceClient(final String serviceURL) {
		this.setServiceURL(serviceURL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity< Facets > listSliceFacets(final String subspace,
			final String sliceKind, final String slice, final String dn) {
		return (ResponseEntity< Facets >) (Object) unifiedGet(Facets.class, getServiceURL()
				+ "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice, dn);
	}

	@Override
	public final ResponseEntity<Void> setSliceConfiguration(final String subspace,
			final String sliceKind, final String slice, final Configuration config, final String dn) {
		return unifiedPut(Void.class, config, getServiceURL() + "/dspace/_"
				+ subspace + "/_" + sliceKind + "/_" + slice, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<Specifier<Void>> transformSlice(final String subspace, final String sliceKind,
			final String slice, final Specifier<Void> newSliceKind, final String dn) {
		return (ResponseEntity<Specifier<Void>>) (Object) unifiedPost(Specifier.class, newSliceKind, getServiceURL() 
				+ "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<Specifier<Facets>> deleteSlice(final String subspace, final String sliceKind,
                                                               final String slice, final String dn) {
		return (ResponseEntity<Specifier<Facets>>) (Object) unifiedDelete(Specifier.class,
				getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity< List< FileStats > > listFiles(
			final String subspace, final String sliceKind, final String slice, final String dn) {
		return (ResponseEntity< List< FileStats > >) (Object) unifiedGet(List.class, getServiceURL()
				+ "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice + "/files", dn);
	}

	@Override
	public final ResponseEntity<Void> deleteFiles(final String subspace, final String sliceKind,
			final String slice, final String dn) {
		return unifiedDelete(getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice + "/files", dn);
	}

	@Override
	public final ResponseEntity<String> getGridFtpUrl(final String subspace,
			final String sliceKind, final String slice, final String dn) {
		return unifiedGet(String.class, getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice + "/gsiftp", dn);
	}

	@Override
	public final ResponseEntity<Void> listFileContent(final String subspace,
                                                      final String sliceKind, final String slice, final String fileName,
                                                      final List<String> attrs, final String dn, final OutputStream out) {
		return unifiedGet(Void.class, getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice + "/_" + fileName, dn);
	}

	@Override
	public final ResponseEntity<Void> setFileContent(final String subspace,
			final String sliceKind, final String slice, final String fileName, 
			final MultipartFile file, final String dn) {
		return unifiedPut(Void.class, file, getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice + "/_" + fileName, dn);
	}

	@Override
	public final ResponseEntity<Void> deleteFile(final String subspace,
			final String sliceKind, final String slice, final String fileName, final String dn) {
		return unifiedDelete(getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice + "/_" + fileName, dn);
	}

}
