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

import java.io.File;
import java.util.List;

import org.springframework.http.ResponseEntity;

import de.zib.gndms.common.dspace.service.SliceService;
import de.zib.gndms.common.kit.dspace.Product;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;
import de.zib.gndms.stuff.confuror.ConfigHolder;

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
	public final ResponseEntity<Product<ConfigHolder, Facets>> listSliceFacets(final String subspace,
			final String sliceKind, final String slice, final String dn) {
		return (ResponseEntity<Product<ConfigHolder, Facets>>) (Object) unifiedGet(Product.class, getServiceURL() 
				+ "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice, dn);
	}

	@Override
	public final ResponseEntity<Void> setSliceConfiguration(final String subspace,
			final String sliceKind, final String slice, final ConfigHolder config, final String dn) {
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

	@Override
	public final ResponseEntity<Void> deleteSlice(final String subspace, final String sliceKind,
			final String slice, final String dn) {
		return unifiedDelete(getServiceURL() + "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice, dn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ResponseEntity<List<File>> listFiles(
			final String subspace, final String sliceKind, final String slice, final List<String> attr,
			final String dn) {
		return (ResponseEntity<List<File>>) (Object) unifiedGet(List.class, getServiceURL() 
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
	public final ResponseEntity<File> listFileContent(final String subspace,
			final String sliceKind, final String slice, final String fileName, final String dn) {
		return unifiedGet(File.class, getServiceURL() + "/dspace/_" + subspace
				+ "/_" + sliceKind + "/_" + slice + "/_" + fileName, dn);
	}

	@Override
	public final ResponseEntity<Void> setFileContent(final String subspace,
			final String sliceKind, final String slice, final String fileName, final File file, final String dn) {
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
