package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.gorfx.types.io.FutureTimeWriter;
import types.FutureTimeT;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
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
