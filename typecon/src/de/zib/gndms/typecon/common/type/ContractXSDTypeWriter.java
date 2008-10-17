package de.zib.gndms.typecon.common.type;

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
public class ContractXSDTypeWriter extends AbstractXSDTypeWriter<OfferExecutionContractT> implements ContractWriter {


    public void writeIfDecisionBefore( Calendar dat ) {
        getProduct( ).setIfDecisionBefore( dat );
    }


    public void writeExecutionLikelyUntil( Calendar dat ) {
        getProduct( ).setExecutionLikelyUntil( dat );
    }


    public void writeConstantExecutionTime( boolean et ) {
        getProduct( ).setConstantExecutionTime( et );
    }


    public void writeResultValidUntil( Calendar dat ) {
        getProduct( ).setResultValidUntil( dat );
    }


    public void begin() {
        setProduct( new OfferExecutionContractT( ) );
    }


    public void done() {
        // Not required here
    }


    public static OfferExecutionContractT fromContract( Contract con ) {

        ContractXSDTypeWriter writ = new ContractXSDTypeWriter();
        ContractConverter conv = new ContractConverter( writ, con );
        conv.convert( );
        return writ.getProduct( );
    }
}
