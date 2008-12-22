package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:05:29
 */
public class FutureTimeConverter extends GORFXConverterBase<FutureTimeWriter, FutureTime> {

	@Override
	public void convert() {
		getWriter().begin();
		FutureTime time = getModel();
		if (time.isAbsolute())
			getWriter().writeAbsoluteFutureTime((FutureTime.AbsoluteFutureTime)time);
		else
			getWriter().writeRelativeFutureTime((FutureTime.RelativeFutureTime)time);
		getWriter().done();
	}
}
