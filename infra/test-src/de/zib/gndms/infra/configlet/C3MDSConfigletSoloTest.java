package de.zib.gndms.infra.configlet;

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



import com.google.common.collect.Maps;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 07.11.2008 Time: 11:28:14
 */
public class C3MDSConfigletSoloTest {
	@Test
	public void testC3MDS() {
		Log log = LogFactory.getLog("test");
		HashMap<String, String> configMap = Maps.newHashMap();
		configMap.put("delay", "10000");
		configMap.put("initialDelay", "1000");
		configMap.put("mdsUrl", "http://c3grid-gt.e-technik.uni-dortmund.de:8080/webmds/webmds?info=indexinfo");

		C3MDSConfiglet configlet = new C3MDSConfiglet();
		configlet.init(log, "c3mds", configMap);
		// configlet.run();

		try {
			Thread.sleep(23000L);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		configlet.shutdown();
	}
}
