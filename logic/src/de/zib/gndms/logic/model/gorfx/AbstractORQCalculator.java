package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.infra.network.GridFTPClientFactory;
import de.zib.gndms.infra.network.NetworkAuxiliariesProvider;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
abstract public class AbstractORQCalculator<M extends AbstractORQ> {

    private Contract perferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;
    private NetworkAuxiliariesProvider networkAuxiliariesProvider;
    

    // here the computation of the required offer should be performed
    public abstract Contract createOffer( ) throws Exception;


    public Contract getPerferredOfferExecution() {
        return perferredOfferExecution;
    }


    public void setPerferredOfferExecution( Contract preferredOfferContract ) {
        this.perferredOfferExecution = preferredOfferContract;
    }


    public M getORQArguments( ) {
        return orqArguments;
    }


    public void setORQArguments( M args ) {
        this.orqArguments = args;
    }


    public Class<M> getORQModelClass( ) {

        return orqModelClass;
    }

    
    protected void setORQModelClass( Class<M> orqModelClass ) {

        this.orqModelClass = orqModelClass;
    }


    public void setJustEstimate( boolean est ) {

        orqArguments.setJustEstimate( true );
    }

    
    public boolean getJustEstimate( ) {

        return orqArguments.getJustEstimate( );
    }


    public NetworkAuxiliariesProvider getNetworkAuxiliariesProvider() {
        return networkAuxiliariesProvider;
    }


    public void setNetworkAuxiliariesProvider( NetworkAuxiliariesProvider networkAuxiliariesProvider ) {
        this.networkAuxiliariesProvider = networkAuxiliariesProvider;
    }
}
