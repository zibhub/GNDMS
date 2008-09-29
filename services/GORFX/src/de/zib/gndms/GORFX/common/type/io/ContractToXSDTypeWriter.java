package de.zib.gndms.GORFX.common.type.io;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractWriter;
import types.OfferExecutionContractT;

import java.util.Calendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:31:25
 */
public class ContractToXSDTypeWriter implements ContractWriter {

    private OfferExecutionContractT product;


    public void writeIfDecisionBefore( Calendar dat ) {
        product.setIfDecisionBefore( dat );
    }


    public void writeExecutionLikelyUntil( Calendar dat ) {
        product.setExecutionLikelyUntil( dat );
    }


    public void writeConstantExecutionTime( boolean et ) {
        product.setConstantExecutionTime( et );
    }


    public void writeResultValidUntil( Calendar dat ) {
        product.setResultValidUntil( dat );
    }


    public void begin() {
        product = new OfferExecutionContractT( );
    }


    public void done() {
        // Not required here
    }


    public OfferExecutionContractT getProduct( ) {
        return product;
    }


    public static OfferExecutionContractT fromContract( Contract con ) {

        ContractToXSDTypeWriter writ = new ContractToXSDTypeWriter();
        ContractConverter conv = new ContractConverter( writ, con );
        conv.convert( );
        return writ.getProduct( );
    }
}
