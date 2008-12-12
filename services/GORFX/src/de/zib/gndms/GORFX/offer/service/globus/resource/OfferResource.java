package de.zib.gndms.GORFX.offer.service.globus.resource;

import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.types.InvalidContractException;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.infra.wsrf.WSConstants;
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

    private String cachedWid;

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
    public @NotNull Task accept() throws InvalidContractException {
        final @NotNull Task task = new Task();
        final @NotNull PersistentContract contract;

        task.setId(getHome().getSystem().nextUUID());

        contract = ContractXSDReader.readContract(getOfferExecutionContract()).acceptNow();
        if( contract.getDeadline() == null )
            contract.setDeadline( WSConstants.getDefaultDeadline() );
        if( contract.getResultValidity() == null )
            contract.setResultValidity( WSConstants.FOREVER );

        if( ! contract.isValid( false ) )
            throw new InvalidContractException( contract );

        task.setContract(contract);
        AbstractORQ orq = getOrqCalc().getORQArguments();
        task.setOrq(orq);
        task.setDescription(orq.getDescription());
        task.setOfferType(getOrqCalc().getKey());
        task.setTerminationTime( contract.getCurrentTerminationTime() );
        task.setWid(WidAux.getWid());
        return task;
    }


    public String getCachedWid() {
        return cachedWid;
    }


    public void setCachedWid(final String cachedWidParam) {
        cachedWid = cachedWidParam;
    }

    @Override
    public void refreshRegistration(final boolean forceRefresh) {
        // nothing
    }

}
