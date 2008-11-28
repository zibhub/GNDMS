package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.TransientContract;
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
public class ContractPropertyReader extends AbstractPropertyReader<TransientContract> {

    public ContractPropertyReader() {
        super(TransientContract.class);
    }


    public ContractPropertyReader(final Properties properties) {
        super(TransientContract.class, properties);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public void read() {
        final @NotNull TransientContract con = getProduct();
        con.setAccepted(PropertyReadWriteAux.readISODateTime(getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key).toDateTimeISO());
        con.setDeadline(PropertyReadWriteAux.readFutureTime(getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key));
        con.setResultValidity(PropertyReadWriteAux.readFutureTime(getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key));
	    final Object sizeProperty = getProperties().get(SfrProperty.EST_MAX_SIZE.key);
	    if (sizeProperty != null) {
		    final String sizePropertyStr = sizeProperty.toString().trim();
		    if (sizePropertyStr.length() > 0)
		        con.setExpectedSize(Long.parseLong(sizePropertyStr));
	    }
    }


    public void done() {
    }
}
