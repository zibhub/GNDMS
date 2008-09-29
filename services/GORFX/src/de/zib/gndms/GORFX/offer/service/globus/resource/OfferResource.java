package de.zib.gndms.GORFX.offer.service.globus.resource;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import de.zib.gndms.model.gorfx.Contract;
import types.OfferExecutionContractT;
import types.DynamicOfferDataSeqT;


/** 
 * The implementation of this OfferResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class OfferResource extends OfferResourceBase {

    private ExtOfferResourceHome home;
    
    // maybe use custom model here
    private Contract contract;


    public void setOfferExecutionContract( OfferExecutionContractT offerExecutionContract ) throws ResourceException {
        super.setOfferExecutionContract( offerExecutionContract );    //To change body of overridden methods use File | Settings | File Templates.
    }


    public void setOfferRequestArguments( DynamicOfferDataSeqT offerRequestArguments ) throws ResourceException {
        super.setOfferRequestArguments( offerRequestArguments );    //To change body of overridden methods use File | Settings | File Templates.
    }


    public ExtOfferResourceHome getHome() {
        return home;
    }


    public void setHome( ExtOfferResourceHome home ) {
        this.home = home;
    }
}
