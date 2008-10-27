package de.zib.gndms.model.gorfx.types.io;

import org.joda.time.DateTime;

import java.util.Calendar;
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


    public void writeIfDecisionBefore(final Calendar dat) {
        PropertyReadWriteAux.writeISODateTime( getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key, new DateTime(dat));
    }


    public void writeExecutionLikelyUntil(final Calendar dat) {
        PropertyReadWriteAux.writeISODateTime( getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key, new DateTime(dat));
    }


    public void writeConstantExecutionTime(final boolean et) {
        getProperties().put(SfrProperty.EST_CONST_EXEC_TIME.key, et ? "true" : "false");
    }


    public void writeResultValidUntil(final Calendar dat) {
        PropertyReadWriteAux.writeISODateTime( getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key, new DateTime(dat));
    }


    public void done() {
    }
}
