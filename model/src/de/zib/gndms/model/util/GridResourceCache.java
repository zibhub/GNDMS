package de.zib.gndms.model.util;

/**
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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
 *
 */


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.NoSuchResourceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.concurrent.TimeUnit;

/**
 * @date:   ${DATE}
 * @time:   ${TIME}
 * @author: Jörg Bachmann
 * @email:  bachmann@zib.de
 */
public class GridResourceCache< G extends GridResource > {
    EntityManagerFactory emf;
    final Class< G > clazz;
    final Cache< String, G > cache;

    public static int MAX_CACHE_SIZE = 1000;

    public GridResourceCache( final Class< G > clazz, final EntityManagerFactory emf ) {
        this.emf = emf;
        this.clazz = clazz;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(12, TimeUnit.HOURS)
                .maximumSize( MAX_CACHE_SIZE )
                .initialCapacity( 0 )
                .build( new GridResourceLoader< G >() );
    }

    public GridResourceCache( final Class< G > clazz ) {
        this.emf = null;
        this.clazz = clazz;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(12, TimeUnit.HOURS)
                .maximumSize( MAX_CACHE_SIZE )
                .initialCapacity( 0 )
                .build( new GridResourceLoader< G >() );
    }

    public boolean exists( String id ) {
        try {
            get( id );
        }
        catch( NoSuchResourceException e ) {
            return false;
        }
        return true;
    }

    public G get( String id ) throws NoSuchResourceException {
        try {
            return cache.get( id );
        }
        catch( Throwable e ) {
            Throwable nested = e.getCause();

            if( nested instanceof NoSuchResourceException )
            {
                throw NoSuchResourceException.class.cast( nested );
            }

            throw new IllegalStateException( "Serious problem while load resource with id " + id + ".", nested );
        }
    }

    public void invalidate( String id ) {
        cache.invalidate( id );
    }

    private class GridResourceLoader< G extends GridResource > extends CacheLoader< String, G > {
        @Override
        @SuppressWarnings("unchecked")
        public G load( final String id ) throws NoSuchResourceException {
            final EntityManager em = emf.createEntityManager();

            G g = ( G )em.find( clazz, id );

            if( null == g )
                throw new NoSuchResourceException( "No " + clazz.getCanonicalName() + " with id " + id + " found." );
            return g;
        }
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
