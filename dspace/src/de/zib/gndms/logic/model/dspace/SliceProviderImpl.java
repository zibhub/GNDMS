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

import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.infra.grams.LinuxDirectoryAux;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.ActionConfigurer;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.ModelUpdateListener;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.util.GridResourceCache;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.gorfx.Taskling;

import org.apache.openjpa.persistence.InvalidStateException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The slice provider which handles the available subspaces providing a mapping
 * of slice ids and slices.
 * 
 * @author Ulrike Golas
 */

public class SliceProviderImpl implements SliceProvider {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private GNDMSystem system;
	private EntityManagerFactory emf;

	private SubspaceProvider subspaceProvider;
	private SliceKindProvider sliceKindProvider;

	private ActionConfigurer actionConfigurer;

	private GridResourceCache<Slice> cache;

	private DirectoryAux directoryAux;

	public SliceProviderImpl(EntityManagerFactory emf) {
		this.actionConfigurer = new ActionConfigurer(emf);
		this.actionConfigurer.setEntityUpdateListener(new Invalidator());
		this.cache = new GridResourceCache<Slice>(Slice.class, emf);
		this.emf = emf;
		this.directoryAux = new LinuxDirectoryAux();
	}

	@Inject
	public void setSystem(GNDMSystem system) {
		this.system = system;
	}

	@Inject
	public void setEntityManagerFactory(final EntityManagerFactory emf) {
		actionConfigurer.setEntityManagerFactory(emf);
		cache.setEmf(emf);
	}

	@Inject
	public void setSubspaceProvider(SubspaceProvider subspaceProvider) {
		this.subspaceProvider = subspaceProvider;
	}

	@Inject
	public void setSliceKindProvider(SliceKindProvider sliceKindProvider) {
		this.sliceKindProvider = sliceKindProvider;
	}

	@Override
	public final boolean exists(final String subspace, final String slice) {
		try {
			cache.get(subspace);
		} catch (NoSuchResourceException e) {
			return false;
		}
		return true;
	}

	@Override
	public final List<String> listSlices(final String subspace)
			throws NoSuchElementException {
		// TODO: query for all slices
		return null;
	}

	@Override
	public final Slice getSlice(final String subspace, final String sliceId)
			throws NoSuchElementException {
		try {
			return cache.get(sliceId);
		} catch (NullPointerException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	public long getDiskUsage(final String subspace, final String sliceId)
			throws NoSuchElementException {
		Slice slice = getSlice(subspace, sliceId);

		final String directory = slice.getSubspace().getPathForSlice(slice);

		return directoryAux.diskUsage(slice.getOwner(), directory);
	}

	public void updateSlice(Slice slice, EntityManager entityManager) {
		final TxFrame txf = new TxFrame(entityManager);
		try {
			entityManager.persist(slice);
			txf.commit();
		} finally {
			txf.finish();
		}
	}

	@Override
	public String createSlice( String subspaceId, String sliceKindId, String dn, DateTime ttm, long sliceSize ) throws NoSuchElementException{

		if (!sliceKindProvider.exists(subspaceId, sliceKindId)) {
			logger.info("Illegal Access: slicekind " + sliceKindId
					+ " in subspace " + subspaceId + " not available.");
			throw new NoSuchElementException("SliceKind " + sliceKindId
					+ " does not exist in subspace " + subspaceId + ".");
		}

		Subspace subspace = subspaceProvider.get(subspaceId);
		SliceKind sliceKind = sliceKindProvider.get(subspaceId, sliceKindId);

		final CreateSliceAction createSliceAction = new CreateSliceAction(dn,
				ttm, sliceKind, sliceSize);
		actionConfigurer.configureAction(createSliceAction);
		system.getInstanceDir().getSystemAccessInjector()
				.injectMembers(createSliceAction);
		createSliceAction.setModel(subspace);
		createSliceAction.setDirectoryAux(new LinuxDirectoryAux());
		Slice slice = null;
		try {
			slice = createSliceAction.call();
		} catch (Exception e) {
			logger.error("couldn't execute transaction " + e);
			//do cleanUp - remove currently created directory on the filesystem if the slice couldn't be commited into database.
			if (createSliceAction.getPath() != null) {
				directoryAux.deleteDirectory(dn, createSliceAction.getPath());
				logger.debug("delete directory " +createSliceAction.getPath() + " due to failed transaction " + e);
			}
			throw new NoSuchElementException("Could not create slice ");
		}

		logger.debug("created slice id: " + slice.getId() + " terminationtime "
				+ slice.getTerminationTime() + " slicesize "
				+ slice.getTotalStorageSize());
		return slice.getId();
	}

	 @Override
	    public String createSlice(
	            final String subspaceId,
	            final String sliceKindId,
	            final String dn,
	            final String localUser,
	            final DateTime ttm,
	            final long sliceSize ) throws NoSuchElementException {

	        if( !sliceKindProvider.exists( subspaceId, sliceKindId ) ) {
	            logger.info( "Illegal Access: slicekind " + sliceKindId + " in subspace " + subspaceId + " not available." );
	            throw new NoSuchElementException( "SliceKind " + sliceKindId + " does not exist in subspace " + subspaceId + "." );
	        }

	        Subspace subspace = subspaceProvider.get( subspaceId );
	        SliceKind sliceKind = sliceKindProvider.get( subspaceId, sliceKindId );

	        final CreateSliceAction createSliceAction = new CreateSliceAction( dn, ttm, sliceKind, sliceSize );
	        actionConfigurer.configureAction( createSliceAction );
	        system.getInstanceDir().getSystemAccessInjector().injectMembers( createSliceAction );
	        createSliceAction.setModel( subspace );
	        createSliceAction.setDirectoryAux( new LinuxDirectoryAux() );

	        Slice slice = null;
			try {
				slice = createSliceAction.call();
			} catch (Exception e) {
				logger.error("couldn't execute transaction " + e);
				//do cleanUp - remove currently created directory on the filesystem if the slice couldn't be commited into database.
				if (createSliceAction.getPath() != null) {
					directoryAux.deleteDirectory(dn, createSliceAction.getPath());
					logger.debug("delete directory " +createSliceAction.getPath() + " due to failed transaction " + e);
				}
				throw new NoSuchElementException("Could not create slice ");
			}

			updateSubspace(subspaceId, slice.getTotalStorageSize());

			
			logger.debug("created slice id: " + slice.getId() + " terminationtime "
					+ slice.getTerminationTime() + " slicesize "
					+ slice.getTotalStorageSize());

	        ChownSliceConfiglet csc = system.getInstanceDir().getConfiglet(ChownSliceConfiglet.class, "sliceChown");

	        logger.debug( "setting owner of " + slice.getId() + " to " + localUser);
	        ProcessBuilderAction chownAct = csc.createChownSliceAction( localUser,
	                slice.getSubspace().getPath() + File.separator + slice.getKind().getSliceDirectory(),
	                slice.getDirectoryId() );
	        logger.debug( "calling " + chownAct.getProcessBuilder().command().toString() );
	        chownAct.getProcessBuilder().redirectErrorStream(true);
	        chownAct.call();
	        logger.debug("Output for chown:" + chownAct.getOutputReceiver().toString());

	        return slice.getId();
	    }

	 
	 private synchronized void updateSubspace(String subspaceID, long sliceSize) {

			Subspace subspace;
			AtomicInteger counter = new AtomicInteger(0);
			counter.incrementAndGet();
			logger.info("counter " + counter);
			EntityManager em = emf.createEntityManager();

			TxFrame tx = new TxFrame(em);

			if (em.getTransaction().isActive()) {

				try {
					subspace = em.find(Subspace.class, subspaceID);
					em.lock(subspace, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
					long newSize = subspace.getAvailableSize() - sliceSize;
					subspace.setAvailableSize(newSize);
			        logger.debug("available space "+newSize);
					em.merge(subspace);
					tx.commit();

				} catch (Exception e) {
					logger.error("Couldn't update subspace " + e);
				}

				finally {
					try {
						tx.finish();
					} catch (Exception e) {
						if (e instanceof InvalidStateException) {

							if (counter.get() <= 3) {
								logger.error("Updating available space failed, retrying... ");
								updateSubspace(subspaceID, sliceSize);
							} else
								throw new IllegalStateException(
										"Updating subspace failed "+e);
						}
						if (em != null && em.isOpen()) {
							em.close();
						}
					}
				}
			}
		
	}

	public Taskling deleteSlice(final String subspaceId, final String sliceId)
			throws NoSuchElementException {

		if (!cache.exists(sliceId)) {
			logger.info("Illegal Access: slice " + sliceId + " in subspace "
					+ subspaceId
					+ " cannot be deleted because it is not available.");
			throw new NoSuchElementException("Slice " + sliceId
					+ " does not exist in subspace " + subspaceId + ".");
		}

		final Subspace subspace = subspaceProvider.get(subspaceId);
		final Slice slice = cache.get(sliceId);

		final DeleteSliceTaskAction deleteAction = new DeleteSliceTaskAction();
		deleteAction.setDirectoryAux(new LinuxDirectoryAux());
		system.getInstanceDir().getSystemAccessInjector()
				.injectMembers(deleteAction);
		deleteAction.setEmf(emf);
		// actionConfigurer.configureAction(deleteAction);

		final Order order = new ModelIdHoldingOrder(sliceId);
		final String wid = UUID.randomUUID().toString();
		logger.info("Delete sliceID (" + sliceId + ") with tracking number "
				+ wid);
		final Taskling ling = system.submitTaskAction(deleteAction, order, wid);

		cache.invalidate(sliceId);

		return ling;
	}

	@Override
	public void invalidate(final String sliceId) {
		cache.invalidate(sliceId);
	}

	private class Invalidator implements ModelUpdateListener<GridResource> {
		public void onModelChange(GridResource model) {
			SliceProviderImpl.this.cache.invalidate(model.getId());
		}
	}

}
