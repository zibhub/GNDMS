package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.gorfx.Contract;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
abstract public class AbstractORQCalculator<M extends AbstractORQ> {

    private Contract perferredOfferExection;
    private Class<M> orqModelClass;
    private M orqArguments;

    // here the computation of the required offer should be performed
    abstract public Contract createOffer( );


    public Contract getPerferredOfferExection() {
        return perferredOfferExection;
    }


    public void setPerferredOfferExection( Contract perferredOfferExection ) {
        this.perferredOfferExection = perferredOfferExection;
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
}
