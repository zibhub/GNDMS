package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import org.globus.wsrf.ResourceException;
import org.apache.axis.types.URI;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.GORFX.common.GORFXTools;
import de.zib.gndms.GORFX.common.type.io.ContractXSDReader;
import de.zib.gndms.infra.system.GNDMSystem;
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


    public void setOfferRequestArguments(types.DynamicOfferDataSeqT offerRequestArguments ) throws ResourceException {

        final GNDMSystem sys = home.getSystem();
        try {
            final URI offerTypeUri = offerRequestArguments.getOfferType();
            ORQCalculator = sys.getInstanceDir().getORQCalculator( sys, sys.getEntityManagerFactory(), offerTypeUri.toString());
            ORQCalculator.setORQArguments( GORFXTools.convertFromORQT( offerRequestArguments ) );
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
}
