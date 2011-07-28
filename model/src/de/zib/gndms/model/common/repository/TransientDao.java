package de.zib.gndms.model.common.repository;

import java.util.Map;
import java.util.NoSuchElementException;
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
 *          Date: 23.12.2010, Time: 12:32:07
 *
 * Transient dao uses a hashmap to store key model associations.
 */
public abstract class TransientDao<K,M,D> implements Dao<K, M, D> {

    private Map<K,M> models;

    public K create( D descriptor ) {
        return create( );
    }


    public void add( M model, K key ) {
        models.put( key, model );
    }


    public M get( K key ) throws NoSuchElementException {

        if( models.containsKey( key ) )
            return models.get( key );

        throw new NoSuchElementException( key.toString() );
    }


    /**
     * This method is used to retrieve new instances of M.
     * @param key
     * @return
     */
    protected abstract M newModel( K key );


    public void deleteByKey( K key ) {
        models.remove( key );
    }


    /**
     * This is expensive, so don't lose your key.
     *
     * @param model The model to delete.
     */
    public void delete( M model ) {
        for ( K key: models.keySet() ) {
            if( models.get( key ).equals( model ) ) {
                deleteByKey( key );
                return;
            }
        }
    }
}
