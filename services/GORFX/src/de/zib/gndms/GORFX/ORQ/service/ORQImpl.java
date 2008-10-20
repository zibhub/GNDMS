package de.zib.gndms.GORFX.ORQ.service;

import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.OfferResource;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceContextImpl;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class ORQImpl extends ORQImplBase {
    private static final Log logger = LogFactory.getLog(ORQImpl.class);

    public ORQImpl() throws RemoteException {
        super();
    }


    @SuppressWarnings({ "FeatureEnvy" })
    public org.apache.axis.message.addressing.EndpointReferenceType getOfferAndDestroyRequest(types.OfferExecutionContractT offerExecutionContract,types.ContextT context) throws RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied {

        try {
            logger.warn("ORQImpl");
            ExtORQResourceHome home = (ExtORQResourceHome ) getResourceHome();
            ResourceContextImpl impl = (ResourceContextImpl) ResourceContext.getResourceContext();
            logger.debug(impl.getServiceURL());
            logger.debug(impl.getResourceKeyHeader());
            logger.debug(impl.getResourceHomeLocation());
            logger.debug(impl.getResourceKey());
            ORQResource orq = home.getAddressedResource();
            ExtOfferResourceHome ohome = ( ExtOfferResourceHome) getOfferResourceHome();
            ResourceKey key = ohome.createResource();
            OfferResource ores = ohome.getResource( key );
            ores.setOfferRequestArguments( orq.getOfferRequestArguments() );
            ores.setOfferExecutionContract(
                ContractXSDTypeWriter.fromContract(
                    orq.getOfferExecutionContract( offerExecutionContract ) ) );
            ores.setOrqCalc(orq.getORQCalculator());

            home.remove( orq.getResourceKey() );
            
            return ohome.getResourceReference( key ).getEndpointReference();
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage(), e);
        }
    }
    

    public types.OfferExecutionContractT permitEstimateAndDestroyRequest(types.OfferExecutionContractT offerExecutionContract,types.ContextT context) throws RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied {

        try {
            ExtORQResourceHome home = (ExtORQResourceHome) getResourceHome();
            ORQResource res = home.getAddressedResource();
            return ContractXSDTypeWriter.fromContract(
                res.estimatedExecutionContract( offerExecutionContract ) );
        } catch ( Exception e ) {
            throw new RemoteException(e.getMessage(), e);
        }

    }

}

