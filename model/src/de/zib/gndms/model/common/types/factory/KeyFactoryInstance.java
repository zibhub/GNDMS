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



import org.jetbrains.annotations.NotNull;


/**
 * A KeyFactoryInstance is created by a KeyFactory.
 *
 * It stores the {@link KeyFactory}, from which it has been created along with the specific key
 * for {@code this}
 * 
 * The first template parameter is the key type, the second template parameter specifies the {@code KeyFactoryInstance} type.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 07.10.2008 Time: 10:58:31
 */
public interface KeyFactoryInstance<K, T extends KeyFactoryInstance<K, T>> {
    /**
     * Returns the {@code KeyFactory} which created {@code this}
     *
     * @return the {@code KeyFactory} which created {@code this}
     */
    KeyFactory<K, T> getFactory();

    /**
     * Sets the KeyFactory, which created {@code this}
     *
     * @param factoryParam the KeyFactory, which created {@code this}
     */
    void setFactory(final @NotNull KeyFactory<K, T> factoryParam);

    /**
     * Returns the key which is needed to return {@code this} when calling {@code getFactory().getInstance(key)}
     * 
     * @return the key which is needed to return {@code this} when calling {@code getFactory().getInstance(key)} 
     */
    K getKey();

    /**
     * Sets the key, which is needed to return {@code this} when calling {@code getFactory().getInstance(key)}
     *
     * @param keyParam the key, which is needed to return {@code this} when calling {@code getFactory().getInstance(key)}
     */
    void setKey(final @NotNull K keyParam);
}
