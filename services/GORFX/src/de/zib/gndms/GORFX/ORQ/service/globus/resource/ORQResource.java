package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import org.globus.wsrf.ResourceException;
import de.zib.gndms.model.gorfx.types.AbstractORQCalculator;
import de.zib.gndms.GORFX.common.GORFXTools;
import de.zib.gndms.infra.system.GNDMSystem;


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
        }
        catch (ClassNotFoundException e) {
            throw new ResourceException(e);
        }
        catch (IllegalAccessException e) {
            throw new ResourceException(e);
        }
        catch (InstantiationException e) {
            throw new ResourceException(e);
        }
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
