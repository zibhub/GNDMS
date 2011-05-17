package de.zib.gndms.GORFX.ORQ.service;

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



import de.zib.gndms.GORFX.ORQ.service.globus.resource.ExtORQResourceHome;
import de.zib.gndms.GORFX.ORQ.service.globus.resource.ORQResource;
import de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied;
import de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest;
import de.zib.gndms.GORFX.offer.service.globus.resource.ExtOfferResourceHome;
import de.zib.gndms.GORFX.offer.service.globus.resource.OfferResource;
import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.util.LogAux;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.model.gorfx.PermissionDeniedORQException;
import de.zib.gndms.logic.model.gorfx.UnfulfillableORQException;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.gritserv.typecon.types.ContractXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceContextImpl;
import types.OfferExecutionContractT;

import java.rmi.RemoteException;


/**
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME -- document yourself 
 *
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class ORQImpl extends ORQImplBase {

    private static final Logger logger = Logger.getLogger(ORQImpl.class);

    public ORQImpl() throws RemoteException {
        super();
    }


    @SuppressWarnings({ "FeatureEnvy" })
    public EndpointReferenceType getOfferAndDestroyRequest(
        types.OfferExecutionContractT offerExecutionContract,types.ContextT context)
        throws RemoteException, UnfullfillableRequest, PermissionDenied
    {

        ORQResource orq = null;
        ExtORQResourceHome home = null;
        try {
            logger.warn("ORQImpl");
            home = (ExtORQResourceHome ) getResourceHome();
            ResourceContextImpl impl = (ResourceContextImpl) ResourceContext.getResourceContext();
            logger.debug(impl.getServiceURL());
            logger.debug(impl.getResourceKeyHeader());
            logger.debug(impl.getResourceHomeLocation());
            logger.debug(impl.getResourceKey());
            logger.debug( "Context: " + LogAux.loggableXSDT( context ) );
            orq = home.getAddressedResource();
        } catch( Exception e ) {
            logger.error( e );
            throw new RemoteException(e.getMessage(), e);
        }

        //  logger.debug( "Default creds: " + GlobusCredential.getDefaultCredential() );
        try {
            WidAux.initWid(orq.getCachedWid());
            WidAux.initGORFXid( orq.getORQCalculator().getORQArguments().getActId() );
            LogAux.logSecInfo( logger, "getOfferAndDestroyRequest" );
            final TransientContract contract = orq.getOfferExecutionContract(offerExecutionContract);
            OfferExecutionContractT oec =
                ContractXSDTypeWriter.write( contract );

            // log contract
            logger.debug( "Calculated contract: " + LogAux.loggableXSDT( oec ) );
            logger.debug( "Creating offer resource" );


            ExtOfferResourceHome ohome = ( ExtOfferResourceHome) getOfferResourceHome();
            ResourceKey key = ohome.createResource();
            OfferResource ores = ohome.getResource( key );
            ores.setCachedWid(WidAux.getWid());
            EndpointReferenceType et = DelegationAux.extractDelegationEPR( context );
            ores.setDelegateEPR( et );
            ores.setOfferRequestArguments( orq.getOfferRequestArguments() );

            ores.setOfferExecutionContract( oec );
            ores.setOrqCalc(orq.getORQCalculator());

            home.remove( orq.getResourceKey() );

            return ohome.getResourceReference( key ).getEndpointReference();

        } catch (UnfulfillableORQException e) {
            logger.error( e );
            throw new UnfullfillableRequest();

        } catch (PermissionDeniedORQException e) {
            logger.error( e );
            throw new PermissionDenied();

        } catch ( Exception e ) {
            logger.error( e );
            throw new RemoteException( "from getOfferAndDestroyRequest", e);
        }
        finally {
            WidAux.removeGORFXid();
            WidAux.removeWid();
        }
    }
    

    public types.OfferExecutionContractT permitEstimateAndDestroyRequest(types.OfferExecutionContractT offerExecutionContract,types.ContextT context) throws RemoteException, UnfullfillableRequest, PermissionDenied {

        try {
            //ContextTAux.initWid(getResourceHome().getModelUUIDGen(), context);
            ExtORQResourceHome home = (ExtORQResourceHome) getResourceHome();
            ORQResource res = home.getAddressedResource();
            WidAux.initWid(res.getCachedWid());
            WidAux.initGORFXid( res.getORQCalculator().getORQArguments().getActId() );
            LogAux.logSecInfo( logger, "permitEstimateAndDestroyRequest" );

            OfferExecutionContractT oec =
                ContractXSDTypeWriter.write( res.estimatedExecutionContract( offerExecutionContract ) );

            // log contract
            logger.debug( "Estimated contract: " + LogAux.loggableXSDT( oec ) );
            home.remove( res.getResourceKey() );
            return oec;
        }
        catch (UnfulfillableORQException e) {
            logger.error( "UnfulfillableORQException: " + e.getMessage(), e );
            throw new UnfullfillableRequest();
        }
        catch (PermissionDeniedORQException e) {
            logger.error( "PermissionDeniedORQException: " + e.getMessage(),  e );
            throw new PermissionDenied();
        }
        catch ( Exception e ) {
            logger.error( "Exception: " + e.getMessage(), e );
            throw new RemoteException( "from permitEstimateAndDestroyRequest", e);
        }
        finally {
            WidAux.removeGORFXid();
            WidAux.removeWid();
        }

    }


    @Override
    public ExtORQResourceHome getResourceHome() throws Exception {
        return (ExtORQResourceHome) super.getResourceHome();    // Overridden method
    }
}

