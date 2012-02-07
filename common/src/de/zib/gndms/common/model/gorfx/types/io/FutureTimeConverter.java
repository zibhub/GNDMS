package de.zib.gndms.common.model.gorfx.types.io;

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



import de.zib.gndms.common.model.gorfx.types.FutureTime;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
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
