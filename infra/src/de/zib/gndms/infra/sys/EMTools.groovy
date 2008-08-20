package de.zib.gndms.infra.sys

import org.jetbrains.annotations.NotNull
import javax.persistence.EntityManager
import javax.persistence.EntityTransaction
import de.zib.gndms.infra.sys.TxSafeRuntimeException

/**
 * THelper code for executing jpa transactions in groovy
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 13.08.2008 Time: 15:24:02
 */
final class EMTools {
    private EMTools() { throw new UnsupportedOperationException("Don't"); }


    public static void txBegin(final @NotNull EntityManager em) {
            em.getTransaction().begin()
    }


    public static void txRollback(final @NotNull EntityManager em) {
            em.getTransaction().rollback()
    }


    public static void txCommit(final @NotNull EntityManager em) {
            em.getTransaction().commit()
    }


    public static boolean txIsActive(final @NotNull EntityManager em) {
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
    public static <T> T txRun(final @NotNull EntityManager em, boolean closeEM,
                              final @NotNull Closure block) {
            final EntityTransaction tx = em.getTransaction();
            final boolean isNewTx = ! tx.isActive();

            final T result
            try {
                    if (isNewTx) {
                            tx.begin();
                            result = (T) block(em)
                            tx.commit()
                    }
                    else
                            result = (T) block(em)
                    return result
            }
            catch (TxSafeRuntimeException re) {
                    throw re.getCause()
            }
            catch (RuntimeException re) {
                    if (tx.isActive()) tx.rollback()
                    throw re
            }
            finally {
                    if (closeEM && em.isOpen()) em.close()
            }
    }
}
