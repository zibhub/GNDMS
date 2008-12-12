package de.zib.gndms.model.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 10.12.2008, Time: 11:06:51
 */
public class EntityManagerAux {

    public static void rollbackAndClose( EntityManager em ) {

        if( em != null &&  em.isOpen() ) {
            try{
                EntityTransaction tx = em.getTransaction();
                if( tx.isActive() )
                    tx.rollback();
            } finally {
                em.close( );
            }
        }
    }
}
