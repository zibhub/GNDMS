package de.zib.gndms.GORFX.service;

import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.GORFX.context.service.globus.resource.ExtTaskResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.service.globus.resource.ExtGORFXResourceHome;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.InstanceDirectory;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class GORFXImpl extends GORFXImplBase {

    private static final Logger logger;
    private final GNDMSystem system;

    static {
        logger = Logger.getLogger(GORFXImpl.class);
    }
    
    @SuppressWarnings({ "FeatureEnvy" })
    public GORFXImpl() throws RemoteException {
        super();
        try {
            final @NotNull ExtGORFXResourceHome home = getResourceHome();
            system = home.getSystem();
            InstanceDirectory instanceDir = system.getInstanceDir();
            instanceDir.addHome(home);
            instanceDir.addHome(getORQResourceHome());
            instanceDir.addHome(getOfferResourceHome());
            instanceDir.addHome(getTaskResourceHome());
        }
        catch (Exception e) {
            e.printStackTrace(System.err);            
            throw new RuntimeException(e);
        }
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


    @Override
    public ExtGORFXResourceHome getResourceHome() throws Exception {
        return (ExtGORFXResourceHome) super.getResourceHome();    // Overridden method
    }


    @Override
    public ExtORQResourceHome getORQResourceHome() throws Exception {
        return (ExtORQResourceHome) super.getORQResourceHome();    // Overridden method
    }


    @Override
    public ExtOfferResourceHome getOfferResourceHome() throws Exception {
        return (ExtOfferResourceHome) super.getOfferResourceHome();    // Overridden method
    }


    @Override
    public ExtTaskResourceHome getTaskResourceHome() throws Exception {
        return (ExtTaskResourceHome) super.getTaskResourceHome();    // Overridden method
    }


    public @NotNull GNDMSystem getSystem() {
        return system;
    }
}

