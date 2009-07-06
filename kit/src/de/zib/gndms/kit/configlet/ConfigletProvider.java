package de.zib.gndms.kit.configlet;

import org.jetbrains.annotations.NotNull;

/**
 *
 * An Interface to provide Configlets
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 18:24:02
 */
public interface ConfigletProvider {
    /**
     * Returns a specific {@code Configlet}
     *
     * @param clazz the class the Configlet belongs to
     * @param name the name of the Configlet
     * @return
     */
    <T extends Configlet> T getConfiglet(@NotNull Class<T> clazz, @NotNull String name);
}
