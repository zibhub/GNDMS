package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:00:34
 */
public interface FutureTimeWriter extends GORFXWriterBase {
	void writeAbsoluteFutureTime(FutureTime.AbsoluteFutureTime t);
	void writeRelativeFutureTime(FutureTime.RelativeFutureTime t);
}
