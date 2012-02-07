package de.zib.gndms.model.common.repository;

import java.util.NoSuchElementException;
import java.util.Queue;
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
 *          Date: 05.01.2011, Time: 17:46:12
 *
 * This isn't a real dao yet but offers the functionality to maintain domain objects in a queue.
 */
public abstract class QueuedDao<K, M, D> implements Dao<K,M,D> {

    private int limit;
    private Queue<K> queue;


    public M get( K key ) throws NoSuchElementException {

        if( queue.size() == limit )
            queue.remove();

        offer( key );
        return provideGet( key );
    }


    /**
     * This should return new models or delegate calls to underlying daos..
     *
     * @param key Model key if required for construction.
     * @return A new model.
     *
     * Note: when this method is called the model is allready inserted into the queue.
     */
    protected abstract M provideGet( K key );


    /**
     * Inserts key into the queue.
     */
    protected void offer( K key ) {
        queue.offer( key );
    }


    /**
     * Sets a new queue element limit.
     * @param newLimit The new limit.
     *
     * Note: if newLimit < limit, Items will be removed from the queue until the queue size is equal to the limit.
     */
    public void setLimit( int newLimit ) {

        while( newLimit < queue.size() )
            remove( );

        this.limit = newLimit;
    }


    protected void remove( ) {
        K k = queue.poll( );
        deleteByKey( k );
    }


    public int getLimit() {
        return limit;
    }
}
