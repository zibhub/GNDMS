package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
