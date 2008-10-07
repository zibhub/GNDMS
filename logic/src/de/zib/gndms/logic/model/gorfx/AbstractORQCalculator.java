package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.factory.Factory;
import de.zib.gndms.kit.factory.FactoryInstance;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
public abstract class AbstractORQCalculator<M extends AbstractORQ, C extends AbstractORQCalculator<M, C>>
    implements FactoryInstance<C> {

    private Contract perferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;
    private NetworkAuxiliariesProvider netAux;
    private Factory<C> factory;

    // here the computation of the required offer should be performed
    public abstract Contract createOffer( ) throws Exception;


    public Contract getPerferredOfferExecution() {
        return perferredOfferExecution;
    }


    public void setPerferredOfferExecution( Contract preferredOfferContract ) {
        perferredOfferExecution = preferredOfferContract;
    }


    public M getORQArguments( ) {
        return orqArguments;
    }


    public void setORQArguments( M args ) {
        orqArguments = args;
    }


    public Class<M> getORQModelClass( ) {

        return orqModelClass;
    }

    
    protected void setORQModelClass( Class<M> orqModelClassParam ) {

        orqModelClass = orqModelClassParam;
    }


    public void setJustEstimate( boolean est ) {

        orqArguments.setJustEstimate( true );
    }

    
    public boolean isJustEstimate( ) {

        return orqArguments.isJustEstimate( );
    }


    public NetworkAuxiliariesProvider getNetAux() {
        return netAux;
    }


    public void setNetAux( NetworkAuxiliariesProvider networkAuxiliariesProvider ) {
        this.netAux = networkAuxiliariesProvider;
    }


    public Factory<C> getFactory() {
        return factory;
    }


    public void setFactory(final Factory<C> factoryParam) {
        factory = factoryParam;
    }
}
