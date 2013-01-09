/*
 * Copyright 2008-2013 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zib.gndms.voldmodel;

import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.stuff.threading.PeriodicalJob;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A VolDRegistrar registers the given site of a type with an end point
 * and additional information at VolD and updates
 * it periodically.
 *
 * @author Joerg Bachmann, Ulrike Golas
 */
public class VolDRegistrar extends PeriodicalJob {
    /**
     * The standard update interval.
     */
    private static final long STANDARD_UPDATE = 60000;

    /**
     * The Adis.
     */
    private Adis adis;

    /**
     * The gorfx endpoint.
     */
    private String gorfxEP;

    /**
     * The name of the site.
     */
    private String name;

    /**
     * The type of the registered site.
     */
    private Type type;

    /**
     * The update interval.
     */
    private long update = STANDARD_UPDATE;

    /**
     * Additional configuration.
     */
    private MapConfig config;

    /**
     * The constructor, registering a site with end point,
     * name and type using the adis.
     * @param adis The adis
     * @param gorfxEP The gorfx end point
     * @param type The type of the site
     * @param name The site name
     */
    public VolDRegistrar(final Adis adis, final String gorfxEP,
           final Type type, final String name) {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
    }

    /**
     * The constructor, registering a site with end point,
     * name and type using the adis and defining the update interval.
     * @param adis The adis
     * @param gorfxEP The gorfx end point
     * @param type The type of the site
     * @param name The site name
     * @param update The update interval
     */
    public VolDRegistrar(final Adis adis, final String gorfxEP,
            final Type type, final String name, final long update) {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
        this.update = update;
    }

    /**
     * The constructor, registering a site with end point,
     * name and type using the adis and defining a configuration.
     * @param adis The adis
     * @param gorfxEP The gorfx end point
     * @param type The type of the site
     * @param name The site name
     * @param config The configuration
     */
    public VolDRegistrar(final Adis adis, final String gorfxEP,
            final Type type, final String name, final MapConfig config) {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
        this.config = config;
        this.update = config.getLongOption("updateInterval", STANDARD_UPDATE);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Long getPeriod() {
        return update;
    }

    @Override
    public final void call() throws Exception {
        if (type.equals(Type.IMPORT)) {
            adis.setImport(name, gorfxEP);
        } else if (type.equals(Type.EXPORT)) {
            adis.setExport(name, gorfxEP);
        } else if (type.equals(Type.CPID_GRAM)) {
            adis.setCP(name, gorfxEP);
        } else if (type.equals(Type.TRANSFER)) {
            adis.setTransfer(name, gorfxEP);
        } else if (type.equals(Type.PUBLISHER)) {
            if (!config.hasOption("oidPrefix")) {
                throw new IllegalStateException(
                        "Dataprovider not configured: no OID_PREFIX given.");
            }
            // register publishing site itself
            adis.setPublisher(name, gorfxEP);

            // also register OID prefix of harvested files
            final Set<String> oidPrefixe = buildSet(config
                    .getOption("oidPrefix"));
            adis.setOIDPrefixe(gorfxEP, oidPrefixe);
        } else if (type.equals(Type.ESGF)) {
            adis.setESGFStager(name, gorfxEP);
        }
    }

    /**
     * Constructs a set of Strings from a given String splitting this String
     * up by the defined dividers.
     * @param s The given String
     * @return The set of Strings
     */
    private static Set<String> buildSet(final String s) {
        // TODO: actually, this should include some logic (or one
        // dividing symbol would be enough)
        return new HashSet<String>(Arrays.asList(s.split("(\\s|,|;)+")));
     }
}
