package de.zib.gndms.model.gorfx.types.io.xml;

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:26:08
 */
public class ORQWrapper {

    private AbstractORQ orq;
    private TransientContract contract;


    public ORQWrapper( ) {
        
    }


    public ORQWrapper( AbstractORQ orq, TransientContract contract ) {
        this.orq = orq;
        this.contract = contract;
    }


    public AbstractORQ getOrq() {
        return orq;
    }


    public void setOrq( AbstractORQ orq ) {
        this.orq = orq;
    }


    public TransientContract getContract() {
        return contract;
    }


    public void setContract( TransientContract contract ) {
        this.contract = contract;
    }
}
