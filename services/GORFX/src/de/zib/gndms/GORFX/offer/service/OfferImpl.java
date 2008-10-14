package de.zib.gndms.GORFX.offer.service;

import de.zib.gndms.GORFX.context.service.globus.resource.ExtTaskResourceHome;
import de.zib.gndms.GORFX.context.service.globus.resource.TaskResource;
import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.OfferResource;
import de.zib.gndms.model.gorfx.Task;
import org.globus.wsrf.ResourceKey;
import org.apache.axis.message.addressing.EndpointReferenceType;

import javax.persistence.EntityManager;
import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class OfferImpl extends OfferImplBase {


    public OfferImpl() throws RemoteException {
        super();
    }

    public org.apache.axis.message.addressing.EndpointReferenceType accept() throws RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied {

        try {
            // Create task object
            ExtOfferResourceHome home = (ExtOfferResourceHome) getResourceHome( );
            OfferResource ores = home.getAddressedResource();
            Task task = ores.accept( );
            ExtTaskResourceHome thome = ( ExtTaskResourceHome) getTaskResourceHome();
            EntityManager em = thome.getEntityManagerFactory().createEntityManager();

            // Persist task object
            em.getTransaction().begin();
            try {
                em.persist(task);
                em.getTransaction().commit();
            }
            finally {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }

            // Create resource and build epr
            EndpointReferenceType epr;
            try {
                final ResourceKey key = thome.getKeyForId(task.getId());
                TaskResource tres = (TaskResource) thome.find(key);
                epr = thome.getResourceReference(key).getEndpointReference();
                return epr;
            }
            catch (RuntimeException e) {
                try {
                    // on failure, try to del the task from the db
                    em.getTransaction().begin();
                    try {
                        if (em.contains(task))
                            em.remove(task);
                        em.getTransaction().commit();
                    }
                    finally {
                        if (em.getTransaction().isActive())
                            em.getTransaction().rollback();
                    }
                }
                finally {
                    // but keep causing exception if even that fails
                    throw e;
                }
            }
        } catch ( Exception e ) {
            throw new RemoteException( e.getMessage() );
        }

    }

}

