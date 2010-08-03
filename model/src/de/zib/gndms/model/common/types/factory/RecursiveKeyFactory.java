package de.zib.gndms.model.common.types.factory;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 16:36:22
 */
public interface RecursiveKeyFactory<K, T extends KeyFactoryInstance<K, T>> extends KeyFactory<K, T>,
	  KeyFactoryInstance<K, RecursiveKeyFactory<K,T>> {
}

