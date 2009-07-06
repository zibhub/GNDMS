package de.zib.gndms.model.common.types.factory;

/**
 * With {@code RecursiveKeyFactory}s it is possible to make KeyFactory-chains.
 * This means a RecursiveKeyFactory is created by another KeyFactory. 
 * So a KeyFactory can now created certain KeyFactories.
 *
 * At the same time, every RecursiveKeyFactory is a KeyFactory for instances being a KeyFactoryInstance.
 *
 * 
 * The first template parameter specifies the type of the keys,
 *  which will be used when a KeyFactoryInstance is created by this factory, but also
 * the key type which is used for the parent RecursiveKeyfactory which created this factory.
 *
 * The second template parameter specifies the {@code KeyFactoryInstance} type this KeyFactory creates.
 *
 * @see KeyFactory
 * @see KeyFactoryInstance
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 16:36:22
 */
public interface RecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> extends KeyFactory<K, T>,
	  KeyFactoryInstance<K, RecursiveKeyFactory<K,T>> {
}

