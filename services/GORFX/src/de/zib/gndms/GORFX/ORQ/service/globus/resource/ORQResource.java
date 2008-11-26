package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.shared.ContextTAux;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import org.apache.axis.types.URI;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
import types.ContextT;
import types.OfferExecutionContractT;


/** 
 * The implementation of this ORQResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class ORQResource extends ORQResourceBase {

    ExtORQResourceHome home;
    AbstractORQCalculator ORQCalculator;
    String cachedWid;


    public void setOfferRequestArguments(types.DynamicOfferDataSeqT offerRequestArguments, ContextT ctx ) throws ResourceException {

        final GNDMSystem sys = home.getSystem();
        try {
            final @NotNull URI offerTypeUri = offerRequestArguments.getOfferType();
            logger.debug("setOfferRequestArguments for offerType: " + offerTypeUri);
            cachedWid = ContextTAux.computeWorkflowId(sys.getModelUUIDGen(), ctx);
            ORQCalculator = sys.getInstanceDir().getORQCalculator( sys, sys.getEntityManagerFactory(), offerTypeUri.toString());
            AbstractORQ orq =  GORFXTools.convertFromORQT( offerRequestArguments, ctx );
            orq.setId( ( String ) getID() );
            ORQCalculator.setORQArguments( orq );
            ORQCalculator.setNetAux( home.getSystem().getNetAux() );
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


    public Contract estimatedExecutionContract( OfferExecutionContractT pref ) throws Exception {

        ORQCalculator.setJustEstimate( true );
        ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );

        return ORQCalculator.createOffer();
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


   public Contract getOfferExecutionContract( OfferExecutionContractT pref ) throws Exception {

       ORQCalculator.setJustEstimate( false );
       ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );

       return ORQCalculator.createOffer();
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

}
