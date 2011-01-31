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
 *          Date: 05.01.2011, Time: 15:35:13
 */
public abstract class QueuedTransientDao<K,M,D> extends QueuedDao<K,M,D> {

    private Dao<K,M,D> dao = new TransientDao<K,M,D> () {
        protected M newModel( K key ) {
            return QueuedTransientDao.this.newModel( key );
        }

        public K create() {
            return QueuedTransientDao.this.create();
        }
    };


    /**
     * Delegated to Transient Dao;
     */
    protected abstract M newModel( K key );


    protected M provideGet( K key ) {
        return dao.get( key );
    }


    public void deleteByKey( K key ) {
        dao.deleteByKey( key );
    }


    public void delete( M model ) {
        dao.delete( model );
    }


    public void add( M model, K key ) {
        dao.add( model, key );
    }


    protected void setDao( Dao<K,M,D> dao ) {
        this.dao = dao;
    }
}
