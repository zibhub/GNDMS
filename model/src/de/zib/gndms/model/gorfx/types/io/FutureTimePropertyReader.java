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
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:11:32
 */
public class FutureTimePropertyReader extends AbstractPropertyReader<FutureTime> {
	private String key;

	public FutureTimePropertyReader() {
		super(FutureTime.class);
	}


	public FutureTimePropertyReader(Properties props) {
		super(FutureTime.class, props);
	}


	public String getKey() {
		return key;
	}


	public void setKey(final String keyParam) {
		key = keyParam;
	}


	@Override
	public FutureTime makeNewProduct() {
		return PropertyReadWriteAux.readFutureTime(getProperties(), key);
	}


	@Override
	public void begin() {
		super.begin();    // Overridden method
		if (key == null)
			throw new IllegalStateException("key missing");
	}


	@Override
	public void read() {

	}


	public void done() {
	}
}
