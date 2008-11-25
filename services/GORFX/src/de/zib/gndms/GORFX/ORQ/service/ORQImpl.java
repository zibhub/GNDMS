package de.zib.gndms.GORFX.ORQ.service;

import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied;
import de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest;
import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.OfferResource;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedORQException;
import de.zib.gndms.logic.model.gorfx.UnfulfillableORQException;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.shared.ContextTAux;
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
            WidAux.initWid(orq.getCachedWid());
            try {
                ExtOfferResourceHome ohome = ( ExtOfferResourceHome) getOfferResourceHome();
                ResourceKey key = ohome.createResource();
                OfferResource ores = ohome.getResource( key );
                ores.setCachedWid(WidAux.getWid());
                ores.setOfferRequestArguments( orq.getOfferRequestArguments() );
                final TransientContract contract = orq.getOfferExecutionContract(offerExecutionContract);
                ores.setOfferExecutionContract(
                    ContractXSDTypeWriter.fromContract(
                            contract) );
                ores.setOrqCalc(orq.getORQCalculator());

                home.remove( orq.getResourceKey() );

                return ohome.getResourceReference( key ).getEndpointReference();
            }
            finally {
                WidAux.removeWid();
            }
        }
        catch (UnfulfillableORQException e) {
            throw new UnfullfillableRequest();
        }
        catch (PermissionDeniedORQException e) {
            throw new PermissionDenied();
        }
        catch ( Exception e ) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage(), e);
        }
    }
    

    public types.OfferExecutionContractT permitEstimateAndDestroyRequest(types.OfferExecutionContractT offerExecutionContract,types.ContextT context) throws RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied {

        try {
            ContextTAux.initWid(getResourceHome().getModelUUIDGen(), context);
            ExtORQResourceHome home = (ExtORQResourceHome) getResourceHome();
            ORQResource res = home.getAddressedResource();
            return ContractXSDTypeWriter.fromContract(
                res.estimatedExecutionContract( offerExecutionContract ) );
        }
        catch (UnfulfillableORQException e) {
            throw new UnfullfillableRequest();
        }
        catch (PermissionDeniedORQException e) {
            throw new PermissionDenied();
        }
        catch ( Exception e ) {
            throw new RemoteException(e.getMessage(), e);
        }
        finally {
            WidAux.removeWid();
        }

    }


    @Override
    public ExtORQResourceHome getResourceHome() throws Exception {
        return (ExtORQResourceHome) super.getResourceHome();    // Overridden method
    }
}

