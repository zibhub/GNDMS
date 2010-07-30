package de.zib.gndms.model.common.types.factory;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
 * A KeyFactory creates and returns a {@link KeyFactoryInstance} corresponding to a specific key.
 *
 * The first template parameter is the key type, the second parameter specifies the type of the KeyFactoryInstance object,
 * which will be created and returned.
 *
 * @see KeyFactoryInstance
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 17:24:24
 */
@SuppressWarnings({ "InterfaceNamingConvention" })
public interface KeyFactory<K, T extends KeyFactoryInstance<K, T>> {

    
    /**
     * Returns the {@code KeyFactoryInstance} object corresponding to {@code key}.
     *
     * @param key a key corresponding to a KeyFactoryInstance object
     * @return the {@code KeyFactoryInstance} object corresponding to {@code key}.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @NotNull T getInstance(@NotNull K key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException;

    void setup();
}
