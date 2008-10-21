package de.zib.gndms.GORFX.offer.service.globus.resource;

import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;
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
    
    private AbstractORQCalculator<?,?> orqCalc;


    @Override
    public void setOfferExecutionContract( OfferExecutionContractT offerExecutionContract ) throws ResourceException {
        super.setOfferExecutionContract( offerExecutionContract );    //To change body of overridden methods use File | Settings | File Templates.
    }



    @Override
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


    @SuppressWarnings({ "FeatureEnvy" })
    public @NotNull Task accept() {
        final @NotNull Task task = new Task();
        final @NotNull Contract contract;
        contract = ContractXSDReader.readContract(getOfferExecutionContract());
        task.setContract(contract);
        AbstractORQ orq = getOrqCalc().getORQArguments();
        task.setOrq(orq);
        task.setDescription(orq.getDescription());
        task.setOfferType(getOrqCalc().getKey());
        task.setTerminationTime(contract.getCurrentTerminationTime());
        task.setId(getHome().getSystem().nextUUID());
        return task;
    }
}
