package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 12:29:27
 */
public class ContractPropertyReader extends AbstractPropertyReader<Contract> {

    public ContractPropertyReader() {
        super(Contract.class);
    }


    public ContractPropertyReader(final Properties properties) {
        super(Contract.class, properties);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public void read() {
        final @NotNull Contract con = getProduct();
        con.setAccepted(PropertyReadWriteAux.readISODateTime(getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key).toDateTimeISO().toGregorianCalendar());
        con.setDeadLine(PropertyReadWriteAux.readISODateTime(getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key).toDateTimeISO().toGregorianCalendar());
        con.setResultValidity(PropertyReadWriteAux.readISODateTime(getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key).toDateTimeISO().toGregorianCalendar());
        con.setDeadlineIsOffset("true".equals(getProperties().get(SfrProperty.EST_CONST_EXEC_TIME.key).toString().toLowerCase().trim()));
    }


    public void done() {
    }
}
