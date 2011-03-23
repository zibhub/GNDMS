package de.zib.gndms.model.common.repository;
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
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 23.12.2010, Time: 11:57:13
 *
 * K - The key type for the model
 * M - The model type.
 * D - A possible descriptor to select a model.
 *
 * The model key seperation is necessary to keep be able to use old model classes w/o key.
 */
public interface Dao<K,M,D> {

    /**
     * Creates a fresh instance of M
     * @return The key of the new instance.
     */
    K create( );

    /**
     * Creates a fresh instance of M matching descirptor.
     *
     * This can be used fine graind construction, i.e. if M is a base class.
     *
     * @param descriptor The descriptor of the object, i.e. class.
     * @return The key of the new instance.
     */
    K create( D descriptor );

    /**
     * Addes a model to the dao.
     * @param model The model to add, with the key to use.
     */
    void add( M model, K key );

    /**
     * Used to retrive an model object form the dao.
     *
     * @param key The key of the object.
     * @return
     */
    M get( K key );

    /**
     * Deletes a model matching the given key.
     * @param key The key.
     *
     * NOTE: Stupid type erasure. 
     */
    void deleteByKey( K key );

    /**
     * Deletes model from the repo.
     * @param model The model to delete.
     *
     * @note M must implement equals.
     */
    void delete( M model );
}
