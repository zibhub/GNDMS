package de.zib.gndms.model.common.repository;

import com.google.common.cache.Cache;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
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

    private Cache<K,M> models;

    public K create( D descriptor ) {
        return create( );
    }


    public void add( M model, K key ) {
        models.asMap().put( key, model );
    }


    public M get( K key ) throws NoSuchElementException {

        if( models.asMap().containsKey( key ) )
            try {
                return models.get( key );
            } catch ( ExecutionException e ) {
                throw new NoSuchElementException( key.toString() );
            }

        throw new NoSuchElementException( key.toString() );
    }


    protected M cacheGet( K key ) {
        try {
            return models.get( key  );
        } catch ( ExecutionException e ) {
            throw new NoSuchElementException( key.toString() );
        }
    }


    protected Map<K, M> getModels() {
        return models.asMap();
    }


    protected void setModels( Cache<K, M> models ) {
        this.models = models;
    }


    public void deleteByKey( K key ) {
        models.invalidate( key );
    }


    /**
     * This is expensive, so don't lose your key.
     *
     * @param model The model to delete.
     */
    public void delete( M model ) {
        for ( K key: models.asMap().keySet() ) {
            try {
                if( models.get( key ).equals( model ) ) {
                    deleteByKey( key );
                    return;
                }
            } catch ( ExecutionException e ) {
                // intentionally
            }
        }
    }
}
