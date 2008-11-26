package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;
import org.joda.time.DateTime;

import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 11:28:28
 */
public class ContractPropertyWriter extends AbstractPropertyIO implements ContractWriter {


    public ContractPropertyWriter() {
        super();
    }


    public ContractPropertyWriter(final Properties properties) {
        super(properties);
    }


    public void writeIfDecisionBefore(final DateTime dat) {
        PropertyReadWriteAux.writeISODateTime( getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key, dat);
    }


	public void writeExpectedSize(final Long l) {
		if (l != null)
			getProperties().put(SfrProperty.EST_MAX_SIZE.key, l.toString());
	}


	public void writeExecutionLikelyUntil(final FutureTime dat) {
        PropertyReadWriteAux.writeFutureTime( getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key, dat);
    }


    public void writeResultValidUntil(final FutureTime dat) {
        PropertyReadWriteAux.writeFutureTime( getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key, dat);
    }


    public void done() {
    }
}
