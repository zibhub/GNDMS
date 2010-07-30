package de.zib.gndms.model.gorfx.types.io;

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

import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:26:42
 */
public class FutureTimePropertyWriter extends AbstractPropertyIO implements FutureTimeWriter {
	private String key;


	public FutureTimePropertyWriter() {
		super();
	}


	public FutureTimePropertyWriter(final Properties properties) {
		super(properties);
	}


	public String getKey() {
		return key;
	}


	public void setKey(final String keyParam) {
		key = keyParam;
	}


	@Override
	public void begin() {
		super.begin();    // Overridden method
		if (getKey() == null)
			throw new IllegalStateException("Missing key");
	}


	public void writeAbsoluteFutureTime(final FutureTime.AbsoluteFutureTime t) {
		PropertyReadWriteAux.writeFutureTime(getProperties(), getKey(), t);
	}


	public void writeRelativeFutureTime(final FutureTime.RelativeFutureTime t) {
		PropertyReadWriteAux.writeFutureTime(getProperties(), getKey(), t);
	}

	public void done() {
	}


}
