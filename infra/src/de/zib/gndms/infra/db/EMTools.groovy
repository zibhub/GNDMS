package de.zib.gndms.infra.db
/**
 * EMTools
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 07.08.2008 Time: 18:09:27
 */
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource
import de.zib.gndms.infra.service.GNDMServiceHome;
import javax.persistence.EntityTransaction;


/**
 * Helper class for dealing with EntityManagers.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.08.2008 Time: 18:04:26
 */
public final class EMTools {
	private static final boolean initialized;

	/**
	 *
	 * Adds all txXXXX-methdos to EntityManager.metaClass and apropriate txRun-Methods to
     * (EntityManagerFactory, EMFactoryProvider, ReloadablePersistentResource).metaClass
	 *
	 * For unknown reasons, this cannot be done inside static {}
 	 */
	def synchronized static boolean initialize() {
		if (initialized)
			return true;

		initialized = true;

		ExpandoMetaClass.enableGlobally();
		EntityManager.metaClass.txBegin =
			{-> return EMTools.txBegin((EntityManager)delegate) }

		EntityManager.metaClass.txCommit =
			{-> return EMTools.txCommit((EntityManager)delegate) }

		EntityManager.metaClass.txRollback =
			{-> return EMTools.txRollback((EntityManager)delegate) }

		EntityManager.metaClass.txIsActive =
			{-> return EMTools.txIsActive((EntityManager)delegate) }

		EntityManager.metaClass.txRun =
			  { Closure block -> return EMTools.txRun((EntityManager)delegate, block) }

		EntityManagerFactory.metaClass.txRun <<
			  { EntityManager em, Closure block ->
				  return EMTools.txRun((EntityManagerFactory)delegate, em, block) }

		EMFactoryProvider.metaClass.txRun =
			  { EntityManager em, Closure block ->
				  return EMTools.txRun((EMFactoryProvider)delegate, em, block) }
		
		ReloadablePersistentResource.metaClass.txRun =
			  { EntityManager em, Closure block ->
				  // home implements EMFactoryProvider
				  GNDMServiceHome home = ((ReloadablePersistentResource) delegate).getResourceHome()
				  return EMTools.txRun(home, em, block)
			  }

		return true;
	}


	def private EMTools() { throw new UnsupportedOperationException("Don't"); }


	def public static void txBegin(final @NotNull EntityManager em) {
		em.getTransaction().begin()
	}


	def public static void txRollback(final @NotNull EntityManager em) {
		em.getTransaction().rollback()
	}


	def public static void txCommit(final @NotNull EntityManager em) {
		em.getTransaction().commit()
	}


	def public static boolean txIsActive(final @NotNull EntityManager em) {
		em.getTransaction().isActive()
	}


	/**
	 * Runs block either with the given EntityManager or if none was provided a newly
	 * created one.  Upon completion of block, the associated transaction is commited.
	 * If a TxSafeRuntimeException is caught, it's cause is rethrown.
	 * If a RuntimeException is caught, it is rethrown after transaction rollback.
	 * If a new EntityManager was created by txRun, it is finally closed before returning
	 * the result of block.
	 *
	 * @param EntityManager em the entity manager to be used
	 * @param closeEM wether em should be closed finally
	 * @param block the Closure to be executed
	 */
	def public static <T> T txRun(final @NotNull EntityManager em, boolean closeEM,
	                              final @NotNull Closure block) {
		final EntityTransaction tx = em.getTransaction();
		final boolean isNewTx = ! tx.isActive();

		try {
			final Object result;
			if (isNewTx) {
				tx.begin();
				result = block(em)
				tx.commit()
			}
			else
				result = block(em)
			return (T) result
		}
		catch (TxSafeRuntimeException re) {
			throw re.getCause()
		}
		catch (RuntimeException re) {
			if (tx.isActive())
				tx.rollback()
			throw re
		}
		finally {
			if (closeEM && em.isOpen())
				em.close()
		}
	}


	/**
	 * Either uses em or that is null obtains a new entity manager and calls
	 *
	 * @see #txRun(EntityManager, boolean, Closure)
	 */
	def public static <T> T txRun(final @NotNull EntityManagerFactory emf, final EntityManager em,
	                              final @NotNull Closure block) {
		if (em == null)
			return txRun(emf.createEntityManager(), true, block)
		else
			return txRun(em, false, block)
	}


	/**
	 * Either uses em or that is null obtains a new entity manager and calls
	 *
	 * @see #txRun(EntityManager, boolean, Closure)
	 */
	def public static <T> T txRun(final @NotNull EMFactoryProvider emfh, final EntityManager em,
	                              final @NotNull Closure block) {
		return txRun(emfh.getEntityManagerFactory(), em, block)
	}
}
