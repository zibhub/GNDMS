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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.zib.gndms.common.dspace.service.SliceService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.common.stuff.util.Product;
import de.zib.gndms.logic.model.dspace.DeleteSliceAction;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceConfiguration;
import de.zib.gndms.logic.model.dspace.SliceKindProvider;
import de.zib.gndms.logic.model.dspace.SliceKindProviderImpl;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.logic.model.dspace.SliceProviderImpl;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import de.zib.gndms.logic.model.dspace.SubspaceProviderImpl;
import de.zib.gndms.logic.model.dspace.TransformSliceAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;

// import de.zib.gndms.neomodel.gorfx.Taskling;

/**
 * The slice service implementation.
 * 
 * @author Ulrike Golas
 */

@Controller
@RequestMapping(value = "/dspace")
public class SliceServiceImpl implements SliceService {

	/**
	 * The logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * The entity manager factory.
	 */
	private EntityManagerFactory emf;
	/**
	 * The entity manager.
	 */
	private EntityManager em;
	/**
	 * The base url, something like \c http://my.host.org/gndms/grid_id.
	 */
	private String baseUrl;
	/**
	 * All available subspaces.
	 */
	private SubspaceProvider subspaceProvider;
	/**
	 * All available slice kinds.
	 */
	private SliceKindProvider sliceKindProvider;
	/**
	 * All available slices.
	 */
	private SliceProvider sliceProvider;
	/**
	 * The facets of a slice.
	 */
	private Facets sliceFacets;
	/**
	 * The uri factory.
	 */
	private UriFactory uriFactory;

	/**
	 * Initialization of the slice service.
	 */
	@PostConstruct
	public final void init() {
		uriFactory = new UriFactory(baseUrl);
		subspaceProvider = new SubspaceProviderImpl();
		sliceKindProvider = new SliceKindProviderImpl();
		sliceKindProvider.init(subspaceProvider);
		sliceProvider = new SliceProviderImpl();
		sliceProvider.init(subspaceProvider);
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}", method = RequestMethod.GET)
	public final ResponseEntity<Product<Configuration, Facets>> listSliceFacets(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			SliceConfiguration config = SliceConfiguration
					.getSliceConfiguration(slic);
			Product<Configuration, Facets> prod2 = new Product<Configuration, Facets>(
					config, sliceFacets);
			return new ResponseEntity<Product<Configuration, Facets>>(prod2,
					headers, HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn("The slice " + slice + " of slice kind " + sliceKind
					+ "does not exist within the subspace" + subspace + ".");
			return new ResponseEntity<Product<Configuration, Facets>>(null,
					headers, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setSliceConfiguration(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestBody final Configuration config,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);

			SliceConfiguration slConfig = SliceConfiguration
					.checkSliceConfig(config);

			// TODO check if we handled all important slice parameters,
			// otherwise SliceConfiguration has to be extended
			slic.setTerminationTime(slConfig.getTerminationTime());
			slic.setTotalStorageSize(slConfig.getSize());

			return new ResponseEntity<Void>(null, headers, HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		} catch (ClassCastException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}", method = RequestMethod.POST)
	public final ResponseEntity<Specifier<Void>> transformSlice(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestBody final Specifier<Void> newSliceKind,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			SliceKind newSliceK = sliceKindProvider.getSliceKind(subspace,
					newSliceKind.getUrl());
			Subspace space = subspaceProvider.getSubspace(subspace);

			em = emf.createEntityManager();
			TxFrame tx = new TxFrame(em);
			try {
				// TODO is this right? what is this uuid generator (last entry)?
				TransformSliceAction action = new TransformSliceAction(
						newSliceK.getURI(), dn, slic.getTerminationTime(),
						newSliceK, space, slic.getTotalStorageSize(), null);
				action.setOwnEntityManager(em);
				logger.info("Calling action for transforming slice " + slice
						+ ".");
				action.call();
				tx.commit();
			} finally {
				tx.finish();
				if (em != null && em.isOpen()) {
					em.close();
				}
			}

			Specifier<Void> spec = new Specifier<Void>();

			HashMap<String, String> urimap = new HashMap<String, String>(2);
			urimap.put("service", "dspace");
			urimap.put(UriFactory.SUBSPACE, subspace);
			urimap.put(UriFactory.SLICEKIND, sliceKind);
			urimap.put(UriFactory.SLICE, slice);
			spec.setUriMap(new HashMap<String, String>(urimap));
			spec.setUrl(uriFactory.quoteUri(urimap));

			return new ResponseEntity<Specifier<Void>>(spec, headers,
					HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}", method = RequestMethod.DELETE)
	public final ResponseEntity<Specifier<Void>> deleteSlice(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);

			em = emf.createEntityManager();
			TxFrame tx = new TxFrame(em);
			try {
				// TODO is this right? what is this uuid generator?
				DeleteSliceAction action = new DeleteSliceAction(slic);
				action.setOwnEntityManager(em);
				logger.info("Calling action for deleting slice " + slice + ".");
				action.call();
				tx.commit();
			} finally {
				tx.finish();
				if (em != null && em.isOpen()) {
					em.close();
				}
			}

			Specifier<Void> spec = new Specifier<Void>();
			// TODO get the task specifier from the action - something like
			// this:
			// Taskling task = new Taskling(???, ???);
			// HashMap<String, String> urimap = new HashMap<String, String>(2);
			// urimap.put(UriFactory.SERVICE, "dspace");
			// urimap.put(UriFactory.SLICEKIND, sliceKind);
			// urimap.put(UriFactory.TASK_ID, task.getId());
			// spec.setUriMap(new HashMap<String, String>(urimap));

			return new ResponseEntity<Specifier<Void>>(spec, headers,
					HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Specifier<Void>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/files", method = RequestMethod.GET)
	public final ResponseEntity<List<File>> listFiles(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestParam(value = "attr", required = false) final Map<String, String> attr,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			String path = space.getPathForSlice(slic);
			File dir = new File(path);
			if (dir.exists() && dir.canRead() && dir.isDirectory()) {
				File[] all = dir.listFiles();
				List<File> files = new ArrayList<File>();
                Collections.addAll( files, all );
				return new ResponseEntity<List<File>>(files, headers,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<File>>(null, headers,
						HttpStatus.FORBIDDEN);
			}
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<List<File>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/files", method = RequestMethod.DELETE)
	public final ResponseEntity<Void> deleteFiles(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			String path = space.getPathForSlice(slic);
			File dir = new File(path);
			if (dir.exists() && dir.canRead() && dir.isDirectory()) {
				File[] all = dir.listFiles();
				boolean allDeleted = true;
				for (File file : all) {
					// TODO: this only works for direct files (no
					// subdirectories)
					allDeleted = allDeleted && file.delete();
				}
				if (allDeleted) {
					return new ResponseEntity<Void>(null, headers,
							HttpStatus.OK);
				} else {
					logger.warn("Some file in directory " + dir
							+ "could not be deleted.");
					return new ResponseEntity<Void>(null, headers,
							HttpStatus.CONFLICT);
				}
			} else {
				logger.warn("Directory " + dir
						+ "cannot be read or is no directory.");
				return new ResponseEntity<Void>(null, headers,
						HttpStatus.FORBIDDEN);
			}
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/gsiftp", method = RequestMethod.GET)
	public final ResponseEntity<String> getGridFtpUrl(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);
		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			return new ResponseEntity<String>(
					space.getGsiFtpPathForSlice(slic), headers, HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<String>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/_{fileName}", method = RequestMethod.GET)
	public final ResponseEntity<OutputStream> listFileContent(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@PathVariable final String fileName,
			@RequestHeader("ATTRS") final List<String> attrs,
			@RequestHeader("DN") final String dn, final OutputStream out) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);
		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			String path = space.getPathForSlice(slic);
			File file = new File(path + File.pathSeparator + fileName);

			if (out == null) {
                final IllegalStateException illegalStateException =
                        new IllegalStateException( "OutputStream not defined." );
                logger.warn( illegalStateException.getMessage() );
                throw illegalStateException;
			}

			if (file.exists() && file.canRead() && file.isFile()) {
				// TODO get requested file attributes

				if (attrs.contains("contents")) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(file)));
					String content = br.readLine();
					br.close();
					out.write(content.getBytes());
				}
				return new ResponseEntity<OutputStream>(out, headers,
						HttpStatus.OK);
			} else {
				logger.warn("File " + file + "cannot be read or is no file.");
				return new ResponseEntity<OutputStream>(null, headers,
						HttpStatus.FORBIDDEN);
			}

		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<OutputStream>(null, headers,
					HttpStatus.NOT_FOUND);
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<OutputStream>(null, headers,
					HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<OutputStream>(null, headers,
					HttpStatus.FORBIDDEN);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/_{fileName}", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setFileContent(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@PathVariable final String fileName,
			@RequestBody final MultipartFile file,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			String path = space.getPathForSlice(slic);
			File newFile = new File(path + File.pathSeparator + fileName);

			if (newFile.exists()) {
				logger.warn("File " + newFile + "will be overwritten. ");			
			}
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile));

			dos.write(file.getBytes());
			dos.close();
			return new ResponseEntity<Void>(null, headers, HttpStatus.OK);
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage(), ne);
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage(), e);
			return new ResponseEntity<Void>(null, headers, HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return new ResponseEntity<Void>(null, headers, HttpStatus.FORBIDDEN);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/_{fileName}", method = RequestMethod.DELETE)
	public final ResponseEntity<Void> deleteFile(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String slice,
			@PathVariable final String fileName,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, slice, dn);

		try {
			Subspace space = subspaceProvider.getSubspace(subspace);
			Slice slic = findSliceOfKind(subspace, sliceKind, slice);
			String path = space.getPathForSlice(slic);
			File file = new File(path + File.pathSeparator + fileName);

			if (file.exists() && file.canWrite() && file.isFile()) {
				if (file.delete()) {
					return new ResponseEntity<Void>(null, headers,
							HttpStatus.OK);
				} else {
					logger.warn("File " + file + "cannot be deleted.");
					return new ResponseEntity<Void>(null, headers,
							HttpStatus.FORBIDDEN);
				}
			} else {
				logger.warn("File " + file + "cannot be written or is no file.");
				return new ResponseEntity<Void>(null, headers,
						HttpStatus.FORBIDDEN);				
			}
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage(), ne);
			return new ResponseEntity<Void>(null, headers, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Sets the GNDMS response header for a given subspace, slice kind, slice
	 * and dn using the base URL.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param sliceKind
	 *            The slice kind id.
	 * @param slice
	 *            The slice id.
	 * @param dn
	 *            The dn.
	 * @return The response header for this subspace.
	 */
	private GNDMSResponseHeader setHeaders(final String subspace,
			final String sliceKind, final String slice, final String dn) {
		GNDMSResponseHeader headers = new GNDMSResponseHeader();
		headers.setResourceURL(baseUrl + "/dspace/_" + subspace + "/_"
				+ sliceKind + "/_" + slice);
		headers.setParentURL(baseUrl + "/dspace/_" + subspace + "/_"
				+ sliceKind);
		if (dn != null) {
			headers.setDN(dn);
		}
		return headers;
	}

	/**
	 * Returns a specific slice of a given slice kind id, if it exists in the
	 * subspace.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param sliceKind
	 *            The slice kind id.
	 * @param slice
	 *            The slice id.
	 * @return The slice.
	 * @throws NoSuchElementException
	 *             If no such slice exists.
	 */
	private Slice findSliceOfKind(final String subspace,
			final String sliceKind, final String slice)
			throws NoSuchElementException {
		Slice slic = sliceProvider.getSlice(subspace, slice);
		SliceKind sliceK = sliceKindProvider.getSliceKind(subspace, sliceKind);

		if (slic.getKind() != sliceK) {
			throw new NoSuchElementException();
		}
		return slic;
	}

	/**
	 * Returns the base url of this slice service.
	 * 
	 * @return the baseUrl
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base url of this slice service.
	 * 
	 * @param baseUrl
	 *            the baseUrl to set
	 */
	public final void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the facets of this slice service.
	 * 
	 * @return the sliceFacets
	 */
	public final Facets getSliceFacets() {
		return sliceFacets;
	}

	/**
	 * Sets the facets of this slice service.
	 * 
	 * @param sliceFacets
	 *            the sliceFacets to set
	 */
	public final void setSliceFacets(final Facets sliceFacets) {
		this.sliceFacets = sliceFacets;
	}
}
