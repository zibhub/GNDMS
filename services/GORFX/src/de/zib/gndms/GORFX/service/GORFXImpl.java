package de.zib.gndms.GORFX.service;

import org.globus.wsrf.ResourceKey;

import java.rmi.RemoteException;

import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.service.globus.resource.GORFXResource;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class GORFXImpl extends GORFXImplBase {


    public GORFXImpl() throws RemoteException {
        super();
    }

    public org.apache.axis.message.addressing.EndpointReferenceType createOfferRequest(types.DynamicOfferDataSeqT offerRequestArguments) throws RemoteException, de.zib.gndms.GORFX.stubs.types.UnsupportedOfferType { //TODO: Implement this autogenerated method

        try{
            ExtORQResourceHome home = (ExtORQResourceHome) getORQResourceHome();
            ResourceKey key = home.createResource();
            ORQResource orqr = (ORQResource) home.find( key );
            orqr.setOfferRequestArguments( offerRequestArguments );

            // todo: Do we have to return the epr isn't the resourceRef sufficient.
            return home.getResourceReference( key ).getEndpointReference();
        } catch ( Exception e ) {
            throw new RemoteException( e.getMessage( ) );
        }
    }

    public org.apache.axis.types.URI[] getSupportedOfferTypes() throws RemoteException {

        try{
            return getResourceHome().getAddressedResource().getSupportedOfferTypes( );
        } catch ( Exception e ) {
            throw new RemoteException( e.getMessage( ) );
        }
    }

    public java.lang.Object callMaintenanceAction(java.lang.String action,types.ContextT options) throws RemoteException {
        //TODO: Implement this autogenerated method
        throw new RemoteException("Not yet implemented");
    }
}

