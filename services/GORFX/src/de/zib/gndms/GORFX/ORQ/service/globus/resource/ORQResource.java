package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import org.globus.wsrf.ResourceException;
import de.zib.gndms.model.gorfx.types.AbstractORQCalculator;
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
            ORQCalculator = sys.getInstanceDir().getORQCalculator( sys.getEntityManagerFactory(), offerRequestArguments.getOfferType( ).toString());
            ORQCalculator.setORQArguments( GORFXTools.convertFromORQT( offerRequestArguments ) );
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


    public Contract estimatedExecutionContract( OfferExecutionContractT pref ) {

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


   public Contract getOfferEcecutionContract( OfferExecutionContractT pref ) {

       ORQCalculator.setJustEstimate( false );
       ORQCalculator.setPerferredOfferExecution( ContractXSDReader.readContract( pref ) );

       return ORQCalculator.createOffer();
    }
}
