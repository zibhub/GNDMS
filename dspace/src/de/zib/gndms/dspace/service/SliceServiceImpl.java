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

import de.zib.gndms.common.dspace.service.SliceService;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.*;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.util.*;

// import de.zib.gndms.neomodel.gorfx.Taskling;

/**
 * The sliceId service implementation.
 * 
 * @author Ulrike Golas
 */

@Controller
@RequestMapping(value = "/dspace")
public class SliceServiceImpl implements SliceService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private EntityManagerFactory emf;
	private EntityManager em;
	private String baseUrl;
	private SubspaceProvider subspaceProvider;
	private SliceKindProvider sliceKindProvider;
	private SliceProvider sliceProvider;
	private Facets sliceFacets;
	private UriFactory uriFactory;

    private GNDMSystem system;

    private RestTemplate restTemplate;

    @Inject
    public void setSliceKindProvider(SliceKindProvider sliceKindProvider) {
        this.sliceKindProvider = sliceKindProvider;
    }

    @Inject
    public void setSliceProvider(SliceProvider sliceProvider) {
        this.sliceProvider = sliceProvider;
    }

    public void setUriFactory(UriFactory uriFactory) {
        this.uriFactory = uriFactory;
    }

    /**
	 * Initialization of the sliceId service.
	 */
	@PostConstruct
	public final void init() {
        setUriFactory( new UriFactory() );
	}

	@Override
	@RequestMapping( value = "/_{subspaceId}/_{sliceKindId}/_{sliceId}", method = RequestMethod.GET )
	public final ResponseEntity< Facets > listSliceFacets(
			@PathVariable final String subspaceId,
			@PathVariable final String sliceKindId,
			@PathVariable final String sliceId,
			@RequestHeader( "DN" ) final String dn ) {
		GNDMSResponseHeader headers = setHeaders( subspaceId, sliceKindId, sliceId, dn );

        try {
            Slice slice = findSliceOfKind( subspaceId, sliceKindId, sliceId );
            return new ResponseEntity< Facets >( sliceFacets, headers, HttpStatus.OK );
        } catch ( NoSuchElementException ne ) {
            logger.warn( "The sliceId " + sliceId + " of sliceId kind " + sliceKindId
                    + "does not exist within the subspace " + subspaceId + "." );
            return new ResponseEntity< Facets >( null,
                    headers, HttpStatus.NOT_FOUND );
        }
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{slice}/config", method = RequestMethod.PUT)
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

			// TODO check if we handled all important sliceId parameters,
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
			SliceKind newSliceK = sliceKindProvider.get(subspace,
					newSliceKind.getUrl());
			Subspace space = subspaceProvider.get(subspace);

			em = emf.createEntityManager();
			TxFrame tx = new TxFrame(em);
			try {
				// TODO is this right? what is this uuid generator (last entry)?
				TransformSliceAction action = new TransformSliceAction(
						dn, slic.getTerminationTime(),
						newSliceK, space, slic.getTotalStorageSize(), null);
				action.setOwnEntityManager(em);
				logger.info("Calling action for transforming sliceId " + slice
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
	@RequestMapping( value = "/_{subspaceId}/_{sliceKindId}/_{sliceId}", method = RequestMethod.DELETE )
	public final ResponseEntity<Specifier<Facets>> deleteSlice(
            @PathVariable final String subspaceId,
            @PathVariable final String sliceKindId,
            @PathVariable final String sliceId,
            @RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspaceId, sliceKindId, sliceId, dn);

		try {
            // submit action
            final Taskling ling = sliceProvider.deleteSlice( subspaceId, sliceId );

            // get service facets of task
            final TaskClient client = new TaskClient( "" );
            client.setRestTemplate( restTemplate );
            final Specifier< Facets > spec = TaskClient.TaskServiceAux.getTaskSpecifier( client, ling.getId(), uriFactory, null, dn );

            // return specifier for service facets
            return new ResponseEntity< Specifier< Facets > >( spec, headers, HttpStatus.OK );
		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Specifier<Facets>>(null, headers,
					HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{sliceId}/files", method = RequestMethod.GET)
	public final ResponseEntity<List<File>> listFiles(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String sliceId,
			@RequestParam(value = "attr", required = false) final Map<String, String> attr,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, sliceId, dn);

		try {
			Subspace space = subspaceProvider.get(subspace);
			Slice slice = findSliceOfKind(subspace, sliceKind, sliceId);
			String path = space.getPathForSlice(slice);
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
			Subspace space = subspaceProvider.get(subspace);
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
			Subspace space = subspaceProvider.get(subspace);
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
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{sliceId}/_{fileName}", method = RequestMethod.GET)
	public final ResponseEntity<Void> listFileContent(
            @PathVariable final String subspace,
            @PathVariable final String sliceKind,
            @PathVariable final String sliceId,
            @PathVariable final String fileName,
            @RequestHeader("ATTRS") final List<String> attrs,
            @RequestHeader("DN") final String dn, final OutputStream out) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, sliceId, dn);
		try {
			Subspace space = subspaceProvider.get(subspace);
			Slice slice = findSliceOfKind(subspace, sliceKind, sliceId);
			String path = space.getPathForSlice(slice);
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
                    FileCopyUtils.copy( new FileInputStream( file ), out );
				}
				return new ResponseEntity<Void>(null, headers,
						HttpStatus.OK);
			} else {
				logger.warn("File " + file + "cannot be read or is no file.");
				return new ResponseEntity<Void>(null, headers,
						HttpStatus.FORBIDDEN);
			}

		} catch (NoSuchElementException ne) {
			logger.warn(ne.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.NOT_FOUND);
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.FORBIDDEN);
		} catch (IOException e) {
			logger.warn(e.getMessage());
			return new ResponseEntity<Void>(null, headers,
					HttpStatus.FORBIDDEN);
		}
	}

	@Override
	@RequestMapping(value = "/_{subspace}/_{sliceKind}/_{sliceId}/_{fileName}", method = RequestMethod.PUT)
	public final ResponseEntity<Void> setFileContent(
			@PathVariable final String subspace,
			@PathVariable final String sliceKind,
			@PathVariable final String sliceId,
			@PathVariable final String fileName,
			@RequestBody final MultipartFile file,
			@RequestHeader("DN") final String dn) {
		GNDMSResponseHeader headers = setHeaders(subspace, sliceKind, sliceId, dn);

		try {
			Subspace space = subspaceProvider.get(subspace);
			Slice slice = findSliceOfKind(subspace, sliceKind, sliceId);
			String path = space.getPathForSlice(slice);
			File newFile = new File(path + File.pathSeparator + fileName);

			if (newFile.exists()) {
				logger.warn("File " + newFile + "will be overwritten. ");			
			}
            
            file.transferTo( newFile );
			
			//DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile));

			//dos.write(file.getBytes());
			//dos.close();
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
			Subspace space = subspaceProvider.get(subspace);
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
	 * Sets the GNDMS response header for a given subspace, sliceId kind, sliceId
	 * and dn using the base URL.
	 * 
	 * @param subspace
	 *            The subspace id.
	 * @param sliceKind
	 *            The sliceId kind id.
	 * @param slice
	 *            The sliceId id.
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
	 * Returns a specific sliceId of a given sliceId kind id, if it exists in the
	 * subspace.
	 * 
	 * @param subspaceId
	 *            The subspace id.
	 * @param sliceKindId
	 *            The sliceId kind id.
	 * @param sliceId
	 *            The sliceId id.
	 * @return The sliceId.
	 * @throws NoSuchElementException
	 *             If no such sliceId exists.
	 */
	private Slice findSliceOfKind( final String subspaceId,
			final String sliceKindId, final String sliceId )
			throws NoSuchElementException {
		Slice slice = sliceProvider.getSlice( subspaceId, sliceId );
		SliceKind sliceK = sliceKindProvider.get( subspaceId, sliceKindId );

		if( !slice.getKind().equals( sliceK ) ) {
            logger.error( "Slice " + sliceId + " is of sliceKind " + slice.getKind().getId() + " instead of " + sliceKindId );
			throw new NoSuchElementException();
		}
		return slice;
	}

	/**
	 * Returns the base url of this sliceId service.
	 * 
	 * @return the baseUrl
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base url of this sliceId service.
	 * 
	 * @param baseUrl
	 *            the baseUrl to set
	 */
	public final void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the facets of this sliceId service.
	 * 
	 * @return the sliceFacets
	 */
	public final Facets getSliceFacets() {
		return sliceFacets;
	}

	/**
	 * Sets the facets of this sliceId service.
	 * 
	 * @param sliceFacets
	 *            the sliceFacets to set
	 */
	public final void setSliceFacets(final Facets sliceFacets) {
		this.sliceFacets = sliceFacets;
	}

    @Inject
    public final void setSubspaceProvider( SubspaceProvider subspaceProvider )
    {
        this.subspaceProvider = subspaceProvider;
    }

    public GNDMSystem getSystem() {
        return system;
    }

    @Inject
    public void setSystem( GNDMSystem system ) {
        this.system = system;
    }

    @Inject
    public void setRestTemplate( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }
}
