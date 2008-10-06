package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.gorfx.Contract;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
public abstract class AbstractORQCalculator<M extends AbstractORQ> {

    private Contract perferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;

    // here the computation of the required offer should be performed
    public abstract Contract createOffer( );


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
}
