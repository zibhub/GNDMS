package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.shared.ContextTAux;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.comserv.delegation.GNDMSCredibleResource;
import de.zib.gndms.comserv.delegation.GNDMSDelegationListener;
import de.zib.gndms.GORFX.action.dms.SliceStageInORQCalculator;
import org.apache.axis.types.URI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.delegation.DelegationUtil;
import org.globus.delegation.DelegationException;
import org.jetbrains.annotations.NotNull;
import org.ietf.jgss.GSSCredential;
import org.gridforum.jgss.ExtendedGSSCredential;
import types.ContextT;
import types.OfferExecutionContractT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


/** 
 * The implementation of this ORQResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class ORQResource extends ORQResourceBase implements GNDMSCredibleResource {

    ExtORQResourceHome home;
    AbstractORQCalculator ORQCalculator;
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

        ORQCalculator.setJustEstimate( true );
        ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );

	    try {
            return ORQCalculator.createOffer();
	    }
	    catch (RuntimeException e) {
		    logger.info(e);
		    throw e;
	    }
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

       ORQCalculator.setJustEstimate( false );
       ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );

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
        storeCredential();
    }


    private void storeCredential() {
        
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

        GNDMSDelegationListener list = new GNDMSDelegationListener( getResourceKey(), home );
        try {
            DelegationUtil.registerDelegationListener(epr, list);
        } catch ( DelegationException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
