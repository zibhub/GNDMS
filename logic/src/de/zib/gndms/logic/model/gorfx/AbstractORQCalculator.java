package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.config.ConfigletProvider;
import de.zib.gndms.kit.factory.Factory;
import de.zib.gndms.kit.factory.FactoryInstance;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.jetbrains.annotations.NotNull;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
public abstract class AbstractORQCalculator<M extends AbstractORQ, C extends AbstractORQCalculator<M, C>>
    implements FactoryInstance<OfferType, AbstractORQCalculator<?, ?>> {

    private ConfigletProvider configletProvider;
    private TransientContract perferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;
    private NetworkAuxiliariesProvider netAux;

    private Factory<OfferType, AbstractORQCalculator<?,?>> factory;
    private OfferType offerType;


    public AbstractORQCalculator() {
    }

    
    public AbstractORQCalculator( Class<M> mc ) {
        orqModelClass = mc;
    }

    // here the computation of the required offer should be performed
    public abstract TransientContract createOffer( ) throws Exception;


    public TransientContract getPreferredOfferExecution() {
        return perferredOfferExecution;
    }


    public void setPerferredOfferExecution( TransientContract preferredOfferTransientContract ) {
        perferredOfferExecution = preferredOfferTransientContract;
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


    public Factory<OfferType, AbstractORQCalculator<?, ?>> getFactory() {
        return factory;
    }


    public void setFactory(
            final @NotNull Factory<OfferType, AbstractORQCalculator<?, ?>> factoryParam) {
        factory = factoryParam;
    }


    public OfferType getKey() {
        return offerType;
    }


    public void setKey(final @NotNull OfferType keyParam) {
        offerType = keyParam;
    }


    public ConfigletProvider getConfigletProvider() {
        return configletProvider;
    }


    public void setConfigletProvider( ConfigletProvider configletProvider ) {
        this.configletProvider = configletProvider;
    }
}
