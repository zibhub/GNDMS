package de.zib.gndms.kit.factory;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 16:36:22
 */
public interface RecursiveFactory<K, T extends FactoryInstance<K, T>> extends Factory<K, T>, FactoryInstance<K, RecursiveFactory<K,T>> {
}
