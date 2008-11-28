package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;
import org.joda.time.DateTime;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:17:43
 */
public interface ContractWriter extends GORFXWriterBase {
    void writeIfDecisionBefore( DateTime dat );
    void writeExecutionLikelyUntil( FutureTime dat );
    void writeExpectedSize( Long l );
    void writeResultValidUntil( FutureTime dat );
}
