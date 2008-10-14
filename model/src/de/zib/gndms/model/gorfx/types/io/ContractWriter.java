package de.zib.gndms.model.gorfx.types.io;

import java.util.Calendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:17:43
 */
public interface ContractWriter extends GORFXWriterBase {

    public void writeIfDecisionBefore( Calendar dat );
    public void writeExecutionLikelyUntil( Calendar dat );
    public void writeConstantExecutionTime( boolean et );
    public void writeResultValidUntil( Calendar dat );
}
