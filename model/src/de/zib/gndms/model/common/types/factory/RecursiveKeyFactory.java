package de.zib.gndms.model.common.types.factory;

/**
 * A {@code RecursiveKeyFactory} is {@code KeyFactory} for {@code KeyFactoryInstances} and a KeyFactoryInstance of a RecursiveKeyFactory.
 *
 * This means an instance of this class can create a {@code KeyFactoryInstance} for a specific key.
 * Besides that the instance is a KeyFactoryInstance, so that it knows the factory (a RecursiveKeyFactory) it has been created of,
 * along with the corresponding key.
 *
 * @see de.zib.gndms.model.common.types.factory.KeyFactory
 * @see de.zib.gndms.model.common.types.factory.KeyFactoryInstance
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 16:36:22
 */
public interface RecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> extends KeyFactory<K, T>,
	  KeyFactoryInstance<K, RecursiveKeyFactory<K,T>> {
}

