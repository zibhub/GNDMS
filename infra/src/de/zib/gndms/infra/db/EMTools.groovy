package de.zib.gndms.infra.db
/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 07.08.2008 Time: 18:09:27
 */
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.model.ModelHandler;


/**
 * Helper class for dealing with EntityManagers.
 *
 * Adds all txXXXX-methdos to EntityManager.metaClass and apropriate txRun-Methods to
 * (EntityManagerFactory, EMFactoryProvider, ReloadablePersistentResource).metaClass
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.08.2008 Time: 18:04:26
 */
public final class EMTools {
	private static final boolean initialized;

	// For unknown reasons, this cannot be done inside static {} 
	def synchronized static void initialize() {
		if (initialized)
			return;
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
			  { Closure block -> return EMTools.txRun((EntityManagerFactory)delegate, block) }

		EMFactoryProvider.metaClass.txRun =
			  { Closure block -> return EMTools.txRun((EMFactoryProvider)delegate, block) }

		ReloadablePersistentResource.metaClass.txRun =
			  { Closure block ->
				  // home implements EMFactoryProvider
				  GNDMServiceHome home = ((ReloadablePersistentResource) delegate).getResourceHome()
				  return EMTools.txRun(home, block)
			  }
	}


	def private EMTools() { throw new UnsupportedOperationException("Don't"); }


	def public static void txBegin(@NotNull final EntityManager em) {
		em.getTransaction().begin()
	}


	def public static void txRollback(@NotNull final EntityManager em) {
		em.getTransaction().rollback()
	}


	def public static void txCommit(@NotNull final EntityManager em) {
		em.getTransaction().commit()
	}


	def public static boolean txIsActive(@NotNull final EntityManager em) {
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
	 */
	def public static <T> T txRun(final EntityManager em, @NotNull final Closure block) {
		if (em) {
			try {
				txBegin(em)
				Object result = block(em)
				txCommit(em)
				return (T) result
			}
			catch (TxSafeRuntimeException re) {
				throw re.getCause()
			}
			catch (RuntimeException re) {
				if (txIsActive(em))
					txRollback(em)
				throw re
			}
			finally {
				if (em.isOpen())
					em.close()
			}
		}
		else
			try {
				block(em)
			}
			catch (TxSafeRuntimeException re) {
				throw re.getCause()
			}
	}


	/**
	 * Obtains an EntityManager if possible and calls:
	 *
	 * @see #txRun(EntityManager, Closure)
	 */
	def public static <T> T txRun(final EntityManagerFactory emf, @NotNull final Closure block) {
		EntityManager em = emf == null ? null : emf.createEntityManager()
		return txRun(em, block)
	}


	/**
	 * Obtains an EntityManager if possible and calls:
	 *
	 * @see #txRun(EntityManager, Closure)
	 */
	def public static <T> T txRun(final EMFactoryProvider emfh, @NotNull final Closure block) {
		EntityManagerFactory factory = emfh == null ? null : emfh.getEntityManagerFactory()
		return txRun(factory, block)
	}
}
