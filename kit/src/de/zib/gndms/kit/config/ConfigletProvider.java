package de.zib.gndms.kit.config;

import de.zib.gndms.kit.configlet.Configlet;
import org.jetbrains.annotations.NotNull;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 18:24:02
 */
public interface ConfigletProvider {

    <T extends Configlet> T getConfiglet(@NotNull Class<T> clazz, @NotNull String name);
}
