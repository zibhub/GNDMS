package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.gorfx.types.io.FutureTimeWriter;
import types.FutureTimeT;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:31:14
 */
public class FutureTimeXSDTypeWriter extends AbstractXSDTypeWriter<FutureTimeT> implements
	  FutureTimeWriter {

	public void writeAbsoluteFutureTime(final FutureTime.AbsoluteFutureTime t) {
		getProduct().setTime(t.getAbsoluteDate().toGregorianCalendar());
	}


	public void writeRelativeFutureTime(final FutureTime.RelativeFutureTime t) {
		// apache axis is complete total utter crap
		// Period period = t.getDuration().toPeriod();
		/* Duration dur = new Duration(false,
		                            period.getYears(), period.getMonths(), period.getDays(), period.getHours(),
		                            period.getMinutes(), (double) period.getSeconds() + (double) (period.getMillis() / 1000)); */
		// while joda time rocks
		// Duration dur = new Duration(false, new DateTime(0L).plus(t.getDuration()).toGregorianCalendar());
		getProduct().setOffset( t.getDuration().getMillis() );
	}


	public void begin() {
        setProduct( new FutureTimeT() );
    }


	public void done() {
	}
}
