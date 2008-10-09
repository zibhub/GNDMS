package de.zib.gndms.GORFX.offer.service.globus.resource;

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.Contract;
import org.globus.wsrf.ResourceException;
import types.DynamicOfferDataSeqT;
import types.OfferExecutionContractT;


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

    private AbstractORQCalculator<?,?> orqCalc;


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


    public AbstractORQCalculator<?, ?> getOrqCalc() {
        return orqCalc;
    }


    public void setOrqCalc(final AbstractORQCalculator<?, ?> orqCalcParam) {
        orqCalc = orqCalcParam;
    }


    public TaskAction<?> accept() {

        // todo: task instantiation therefor
        //  identify task action to use
        //  add relevant data orq data and contract to the task
        //  use system to trigger task execution NOP done by TaskResource
        // todo: set contract for action
        //  return task
        return null;
        
    }
}
