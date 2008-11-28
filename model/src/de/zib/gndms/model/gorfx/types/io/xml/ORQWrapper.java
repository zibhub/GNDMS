package de.zib.gndms.model.gorfx.types.io.xml;

import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.Contract;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:26:08
 */
public class ORQWrapper {

    private AbstractORQ orq;
    private Contract contract;


    public ORQWrapper( ) {
        
    }


    public ORQWrapper( AbstractORQ orq, Contract contract ) {
        this.orq = orq;
        this.contract = contract;
    }


    public AbstractORQ getOrq() {
        return orq;
    }


    public void setOrq( AbstractORQ orq ) {
        this.orq = orq;
    }


    public Contract getContract() {
        return contract;
    }


    public void setContract( Contract contract ) {
        this.contract = contract;
    }
}
