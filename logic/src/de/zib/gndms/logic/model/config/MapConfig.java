package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Collections;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 06.10.2008 Time: 11:21:58
*/
public class MapConfig extends AbstractConfig {
    private final Map<String, String> map;


    public MapConfig(final Map<String, String> mapParam) {
        super();
        map = Collections.unmodifiableMap(mapParam);
    }


    public String getNonMandatoryOption(final String nameParam) {
        return map.get(nameParam);
    }


    public boolean hasOption(final @NotNull String name) {
        return map.containsKey(name);
    }
}
