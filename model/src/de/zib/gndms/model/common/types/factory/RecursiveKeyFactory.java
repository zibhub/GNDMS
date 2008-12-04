package de.zib.gndms.model.common.types.factory;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 16:36:22
 */
public interface RecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> extends KeyFactory<K, T>,
	  KeyFactoryInstance<K, RecursiveKeyFactory<K,T>> {
}
