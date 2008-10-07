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
public interface FactoryInstance<T extends FactoryInstance<T>> {
    Factory<T> getFactory();
    void setFactory(final @NotNull Factory<T> factoryParam);
}
