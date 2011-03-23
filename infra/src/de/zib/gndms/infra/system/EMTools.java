package de.zib.gndms.infra.system;

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



import com.google.common.base.Function;
import de.zib.gndms.stuff.exception.FinallyException;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Helper code for executing jpa transactions in groovy
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 13.08.2008 Time: 15:24:02
 */
final public class EMTools {
    private EMTools() { throw new UnsupportedOperationException("Don't"); }


    public static void txBegin(final @NotNull EntityManager em) {
            em.getTransaction().begin();
    }


    public static void txRollback(final @NotNull EntityManager em) {
            em.getTransaction().rollback();
    }


    public static void txCommit(final @NotNull EntityManager em) {
            em.getTransaction().commit();
    }


    public static boolean txIsActive(final @NotNull EntityManager em) {
            return em.getTransaction().isActive();
    }

    /**
     * Runs block either with the given EntityManager or if none was provided a newly
     * created one.  Upon completion of block, the associated transaction is commited.
     * If a TxSafeRuntimeException is caught, it's cause is rethrown.
     * If a RuntimeException is caught, it is rethrown after transaction rollback.
     * If a new EntityManager was created by txRun, it is finally closed before returning
     * the result of block.
     *
     * @param em EntityManager em the entity manager to be used
     * @param closeEM wether em should be closed finally
     * @param block the Closure to be executed
     * @return Result of executing block in transaction context
     */
    @SuppressWarnings({"ThrowFromFinallyBlock"})
    public static <T> T txRun(final @NotNull EntityManager em, boolean closeEM,
                              final @NotNull Function<EntityManager, T> block) {
        final EntityTransaction tx = em.getTransaction();
        final boolean isNewTx = ! tx.isActive();

        final T result;
        RuntimeException ex = null;
        try {
            if (isNewTx) {
                tx.begin();
                result = block.apply(em);
                tx.commit();
            }
            else
                result = block.apply(em);
            return result;
        }
        catch (TxSafeRuntimeException re) {
            ex = new RuntimeException( re );
        }
        catch (RuntimeException re) {
            if (tx.isActive()) tx.rollback();
            ex = re;
        }
        finally {
            // w/o try catch this often shadows above exceptions
            // so wrap in finally
            try {
                if (closeEM && em.isOpen()) em.close();
            }
            catch ( RuntimeException e ) {
                if( ex != null )
                    throw new FinallyException( "From finally", ex, e );
                else
                    throw e;
            }
        }
        if( ex != null )
            throw ex;
        throw new IllegalStateException("Supposedly Unreachable");
    }

    // TODO: Integrate this with GridEntityModelHandler, requires better exception handling in AbstractEntityModelAction
    public static <T> T txResRun(final @NotNull EntityManager em, boolean closeEM,
                              final @NotNull Function<EntityManager, T> block) throws ResourceException {
        try {
            return txRun(em, closeEM, block);
        }
        catch (RuntimeException e) {
            final Throwable cause = e.getCause();
            if (e == null || !(cause instanceof ResourceException))
                throw e;
            else
                throw (ResourceException) cause;
        }
    }
}
