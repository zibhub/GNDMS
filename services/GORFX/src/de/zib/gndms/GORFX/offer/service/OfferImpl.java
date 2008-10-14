package de.zib.gndms.GORFX.offer.service;

import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.OfferResource;
import de.zib.gndms.GORFX.context.service.globus.resource.ExtTaskResourceHome;
import de.zib.gndms.GORFX.context.service.globus.resource.TaskResource;
import de.zib.gndms.logic.model.TaskAction;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceKey;

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
            ExtOfferResourceHome home = (ExtOfferResourceHome) getResourceHome( );

            OfferResource ores = home.getAddressedResource();
            TaskAction ta = ores.accept( );
            ExtTaskResourceHome thome = ( ExtTaskResourceHome) getTaskResourceHome();
            ResourceKey key = thome.createResource ( );
            TaskResource tres = thome.getResource( key );
            tres.setTaskAction( ta );

            return thome.getResourceReference( key ).getEndpointReference();
        } catch ( Exception e ) {
            throw new RemoteException( e.getMessage() );
        }

    }

}

