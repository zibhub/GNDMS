package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 16.10.2008 Time: 14:34:29
 */
@SuppressWarnings({ "MethodMayBeStatic" })
public class MapConfigTest {


    @Test
    public void testMapConfig() throws MandatoryOptionMissingException {
        Map<String, String> map = new HashMap<String, String>(8);
        map.put("HOME", "%{HOME}");
        map.put("HOME2", "foo%{HOME}bar");
        MapConfig config = new MapConfig(map);
        assert config.getOption("HOME").equals(System.getProperties().getProperty("user.home"));
        assert config.getOption("HOME2").equals("foo"+System.getProperties().getProperty("user.home")+"bar");
    }
}
