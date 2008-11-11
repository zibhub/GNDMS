package de.zib.gndms.infra.configlet;

import com.google.common.collect.Maps;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.11.2008 Time: 11:28:14
 */
public class C3MDSConfigletSoloTest {
	@Test
	public void testC3MDS() {
		Log log = LogFactory.getLog("test");
		HashMap<String, String> configMap = Maps.newHashMap();
		configMap.put("delay", "5000");
		configMap.put("initialDelay", "1000");
		configMap.put("mdsUrl", "http://c3grid-gt.e-technik.uni-dortmund.de:8080/webmds/webmds?info=indexinfo");

		C3MDSConfiglet configlet = new C3MDSConfiglet();
		configlet.init(log, "c3mds", configMap);
		configlet.run();

		try {
			Thread.sleep(5000L);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		configlet.shutdown();
	}
}
