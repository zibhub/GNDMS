package de.zib.gndms.GORFX.offer.service;

import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;

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
            ExtOfferResourceHome orh = (ExtOfferResourceHome) getResourceHome( );
            
        } catch ( Exception e ) {
            throw new RemoteException( e.getMessage() );
        }

    }

}

