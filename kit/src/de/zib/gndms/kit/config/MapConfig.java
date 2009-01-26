package de.zib.gndms.kit.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Collections;
import java.util.Iterator;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 06.10.2008 Time: 11:21:58
*/
public class MapConfig extends AbstractConfig {
    /**
     * the map containing the keys and their corresponding values
     */
    private final Map<String, String> map;


    public MapConfig(final Map<String, String> mapParam) {
        super();
        map = Collections.unmodifiableMap(mapParam);
    }


    @Override
    public String getConcreteNonMandatoryOption(final String nameParam) {
        return map.get(nameParam);
    }


    public boolean hasOption(final @NotNull String name) {
        return map.containsKey(name);
    }

    /**
     * Returns an iterator over the keys of the configuration
     * @return an iterator over the keys of the configuration
     */
	public Iterator<String> iterator() {
		return Collections.unmodifiableSet(map.keySet()).iterator();
	}
}
