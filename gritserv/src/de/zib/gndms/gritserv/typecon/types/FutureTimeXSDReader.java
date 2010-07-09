package de.zib.gndms.gritserv.typecon.types;

import de.zib.gndms.model.common.types.FutureTime;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import types.FutureTimeT;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 18:04:31
 */
public class FutureTimeXSDReader {
	private FutureTimeXSDReader() {}


	public static FutureTime read(FutureTimeT ft) {
		if (ft == null)
			return null;
		if (ft.getTime() == null) {
			return FutureTime.atOffset( new Duration( ft.getOffset() ) );
		}
		else
			return FutureTime.atTime(new DateTime(ft.getTime()));
	}

}
