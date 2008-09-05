package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import org.globus.wsrf.ResourceException;
import de.zib.gndms.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.GORFX.common.GORFXTools;


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

        ORQCalculator = home.getSystem( ).createORQCalculator( offerRequestArguments.getOfferType( ).toString() );
        ORQCalculator.setORQArguments( GORFXTools.convertFromORQT( offerRequestArguments ) );

        super.setOfferRequestArguments( offerRequestArguments );
    }


    public AbstractORQCalculator getORQWorker() {
        return ORQCalculator;
    }


    public ExtORQResourceHome getHome() {
        return home;
    }


    public void setHome( ExtORQResourceHome home ) {
        this.home = home;
    }
}
