package de.zib.gndms.logic.model.dspace;

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

import de.zib.gndms.common.model.gorfx.types.VoidTaskResult;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.ModelTaskAction;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;

import org.apache.openjpa.persistence.InvalidStateException;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 20.12.11 11:44
 * @brief
 */
public class DeleteSliceTaskAction extends ModelTaskAction<ModelIdHoldingOrder> {

	private SubspaceProvider subspaceProvider;
	private SliceProvider sliceProvider;
	private final AtomicInteger counter = new AtomicInteger();


	public DeleteSliceTaskAction() {

		super(ModelIdHoldingOrder.class);
	}

	@Override
	protected void onCreated(@NotNull final String wid,
			@NotNull final TaskState state, final boolean isRestartedTask,
			final boolean altTaskState) throws Exception {

		if (!isRestartedTask) {
			final Session session = getDao().beginSession();
			try {
				final Task task = getModel().getTask(session);
				task.setWID(wid);
				task.setMaxProgress(1);
				task.setProgress(0);
				session.success();
			} finally {
				session.finish();
			}
		}
		super.onCreated(wid, state, isRestartedTask, altTaskState);
	}

	@Override
	protected synchronized void onInProgress(@NotNull final String wid,
			@NotNull final TaskState state, final boolean isRestartedTask,
			final boolean altTaskState) throws Exception {
		ensureOrder();

		Slice slice = getModelEntity(Slice.class);

		long sliceSize = slice.getTotalStorageSize();
		String subspaceID = slice.getSubspace().getId();

		logger.debug("subspaceID " + subspaceID);
		logger.debug("getModel getID " + getModel().getId());

		// Subspace sp = em.find( Subspace.class, getModel().getId() );

		if (null == slice){
			throw new IllegalArgumentException("Could not find slice "
					+ getOrder().getModelId());
		}
		try {
			ChownSliceConfiglet csc = getConfigletProvider().getConfiglet(
					ChownSliceConfiglet.class, "sliceChown");
			String gndmsUser = System.getProperty("user.name");
			logger.debug("changing owner of " + slice.getId() + " to "
					+ gndmsUser);
			slice.setOwner(gndmsUser);
			ProcessBuilderAction chownAct = csc.createChownSliceAction(
					gndmsUser, slice.getSubspace().getPath() + File.separator
							+ slice.getKind().getSliceDirectory(),
					slice.getDirectoryId());
			logger.debug("calling "
					+ chownAct.getProcessBuilder().command().toString());
			chownAct.getProcessBuilder().redirectErrorStream(true);
			chownAct.call();
			logger.debug("Output for chown:"
					+ chownAct.getOutputReceiver().toString());

			logger.debug("Delete directory: "
					+ slice.getSubspace().getPathForSlice(slice));
			getDirectoryAux().deleteDirectory(slice.getOwner(),
					slice.getSubspace().getPathForSlice(slice));
		} catch (NoSuchResourceException e) {
			logger.info("Tried to delete non existing directory "
					+ slice.getSubspace().getPathForSlice(slice));
		} catch (RuntimeException e) {
			logger.error(
					"Could not delete directory of slice "
							+ slice.getId()
							+ ". Nevertheless, the slice will be deleted from database!",
					e);
		}

		try {
			final String metaFileFilter = slice.getDirectoryId() + "[_\\.].*";
			getDirectoryAux().deleteFiles(
					slice.getOwner(),
					slice.getSubspace().getPath() + File.separatorChar
							+ slice.getKind().getSliceDirectory(),
					metaFileFilter);
		} catch (NoSuchResourceException e) {
			// ok, no meta file there...
		} catch (RuntimeException e) {
			logger.error(
					"Could not delete metaFile of slice "
							+ slice.getId()
							+ ". Nevertheless, the slice will be deleted from database!",
					e);
		}

		deleteModelEntity(Slice.class);
		sliceProvider.invalidate(slice.getId());

		updateSubspace(subspaceID, sliceSize);

		autoTransitWithPayload(new VoidTaskResult());
	}

	private synchronized void updateSubspace(String subspaceID, long sliceSize) {

		Subspace subspace;
		counter.incrementAndGet();

		EntityManager em = getEmf().createEntityManager();

		TxFrame tx = new TxFrame(em);

		if (em.getTransaction().isActive()) {

			try {
				subspace = em.find(Subspace.class, subspaceID);
				long newSize = subspace.getAvailableSize() + sliceSize;
				subspace.setAvailableSize(newSize);
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
						logger.error("InvalidStateException: Couldn't update subspace "
								+ e);
						if (counter.get() < 3) {
							logger.error("retry to update available space... ");
							updateSubspace(subspaceID, sliceSize);
						} else
							throw new IllegalStateException(
									"Updating subspace failed");
					}
					if (em != null && em.isOpen()) {
						em.close();
					}
				}
			}
		}

	}

	private void updateSubspace(Subspace sp) {
		EntityManager em = getEmf().createEntityManager();
		TxFrame tx = new TxFrame(em);
		try {
			em.merge(sp);
			tx.commit();
		} finally {
			tx.finish();
		}

	}

	@Inject
	public void setSliceProvider(SliceProvider sliceProvider) {
		this.sliceProvider = sliceProvider;
	}

	@Inject
	public void setSubspaceProvider(SubspaceProvider subspaceProvider) {
		this.subspaceProvider = subspaceProvider;
	}
}