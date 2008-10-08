package de.zib.gndms.kit.factory;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.10.2008 Time: 10:58:31
 */
public interface FactoryInstance<K, T extends FactoryInstance<K, T>> {
    Factory<K, T> getFactory();
    void setFactory(final @NotNull Factory<K, T> factoryParam);

    K getKey();
    void setKey(final @NotNull K keyParam);
}
