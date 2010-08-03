package de.zib.gndms.GORFX.ORQ.service.globus.resource;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.GORFX.action.dms.SliceStageInORQCalculator;
import de.zib.gndms.gritserv.delegation.GNDMSCredibleResource;
import de.zib.gndms.gritserv.delegation.GNDMSDelegationListener;
import de.zib.gndms.gritserv.typecon.GORFXTools;
import de.zib.gndms.gritserv.typecon.types.ContractXSDReader;
import de.zib.gndms.gritserv.typecon.util.ContextTAux;
import de.zib.gndms.gritserv.util.GlobusCredentialProviderImpl;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.delegation.DelegationException;
import org.globus.delegation.DelegationUtil;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.wsrf.ResourceException;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;
import org.jetbrains.annotations.NotNull;
import types.ContextT;
import types.OfferExecutionContractT;

import java.io.File;
import java.io.FileOutputStream;


/** 
 * The implementation of this ORQResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class ORQResource extends ORQResourceBase implements GNDMSCredibleResource {

    transient ExtORQResourceHome home;
    transient AbstractORQCalculator ORQCalculator;
    String cachedWid;
    GlobusCredential credential;


    public void setOfferRequestArguments(types.DynamicOfferDataSeqT offerRequestArguments, ContextT ctx ) throws ResourceException {

        final GNDMSystem sys = home.getSystem();
        try {
            final @NotNull URI offerTypeUri = offerRequestArguments.getOfferType();
            logger.debug("setOfferRequestArguments for offerType: " + offerTypeUri);
            cachedWid = ContextTAux.computeWorkflowId(sys.getModelUUIDGen(), ctx);
            ORQCalculator = sys.getInstanceDir().newORQCalculator( sys.getEntityManagerFactory(), offerTypeUri.toString());
            AbstractORQ orq =  GORFXTools.convertFromORQT( offerRequestArguments, ctx );
            orq.setActId( ( String ) getID() );
            ORQCalculator.setORQArguments( orq );
            //ORQCalculator.setNetAux( home.getSystem().getNetAux() );
        }
        catch (ClassNotFoundException e) {
            throw new ResourceException(e);
        }
        catch (IllegalAccessException e) {
            throw new ResourceException(e);
        }
        catch (InstantiationException e) {
            throw new ResourceException(e);
        } catch (Exception e) {
            throw new ResourceException(e);
        }

        super.setOfferRequestArguments( offerRequestArguments );
    }


    public TransientContract estimatedExecutionContract( OfferExecutionContractT pref ) throws Exception {

        return doExecutionContract( pref, false );
    }


    public AbstractORQCalculator getORQCalculator() {
        return ORQCalculator;
    }


    public ExtORQResourceHome getHome() {
        return home;
    }


    public void setHome( ExtORQResourceHome home ) {
        this.home = home;
    }


   public TransientContract getOfferExecutionContract( OfferExecutionContractT pref ) throws Exception {

       return doExecutionContract( pref, false );
    }


    protected TransientContract doExecutionContract( OfferExecutionContractT pref, boolean est ) throws Exception {

        ORQCalculator.setJustEstimate( est );
        ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );
        ORQCalculator.setCredentialProvider(
            new GlobusCredentialProviderImpl( ORQCalculator.getKey().getOfferTypeKey(), credential ) );

        if( ORQCalculator instanceof SliceStageInORQCalculator ) {
            ( (SliceStageInORQCalculator) ORQCalculator ).setCredential ( credential );
        }

        try {
            return ORQCalculator.createOffer();
        }
        catch (RuntimeException e) {
            logger.info(e);
            throw e;
        }
    }


    public String getCachedWid() {
        return cachedWid;
    }


    public void setCachedWid(final String widParam) {
        cachedWid = widParam;
    }

    @Override
    public void refreshRegistration(final boolean forceRefresh) {
        // nothing
    }


    public void setCredential( final GlobusCredential cred ) {
        System.out.println( "setCredential called with " + cred );
        credential = cred;
   //     storeCredential();
    }


    public void storeCredential() {
        
        try {
            File f = new File( "/tmp/" + (String) getID() );
            FileOutputStream fos = new FileOutputStream( f );
           // ObjectOutputStream oos = new ObjectOutputStream( fos );
           // credential.save( oos );
            GlobusGSSCredentialImpl crd = new GlobusGSSCredentialImpl( credential, GSSCredential.ACCEPT_ONLY );
            fos.write( crd.export( ExtendedGSSCredential.IMPEXP_OPAQUE  ) );
            fos.close();
        } catch( Exception e ) {
            logger.error( e );
        }
    }


    public GlobusCredential getCredential() {
        return credential;
    }


    public void setDelegateEPR( final EndpointReferenceType epr ) {

        // this gives wron gorfx path
        //String servicePath =  org.apache.axis.MessageContext.getCurrentContext().getTargetService();
        String servicePath = "gndms/ORQ/home";
        String homeName = org.globus.wsrf.Constants.JNDI_SERVICES_BASE_NAME + servicePath;
        GNDMSDelegationListener list = new GNDMSDelegationListener( getResourceKey(), homeName );
        try {
            DelegationUtil.registerDelegationListener(epr, list);
        } catch ( DelegationException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
