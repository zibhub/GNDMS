package de.zib.gndms.logic.model.config;

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



import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * Tests whether MapConfig correctly handles system enviroment variables, denoted in key's value
 *
 * @author: try ste fan pla nti kow zib
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
