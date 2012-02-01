/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.model.dspace.Slice;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date: 01.02.12
 * @time: 11:59
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SliceReaper {
    public static final String LIST_ALL_SLICES = "listAllSlices";

    private long intervall; // in milliseconds
    private boolean done = false;

    protected EntityManagerFactory emf;
    protected SliceProvider sliceProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final @NotNull Lock stopLock = new ReentrantLock();
    private final @NotNull Condition stopCond = stopLock.newCondition();


    private void reap() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( LIST_ALL_SLICES );
        for( Object o : query.getResultList() ) {
            final Slice slice = Slice.class.cast( o );

            if( null == slice ) {
                // this is not happening
                continue;
            }
            
            if( slice.getTerminationTime().isAfterNow() ) {
                continue;
            }

            try {
                sliceProvider.deleteSlice( slice.getSubspace().getId(), slice.getId() );
            } catch (NoSuchElementException e) {
                logger.error( "Could not delete slice " + slice.getId(), e );
            }
        }
    }
    
    @PostConstruct
    public void init() {
        Thread worker = new Thread( new Runnable() {
            @Override
            public void run() {
                while( true ) {
                    SliceReaper.this.reap();

                    try {
                        stopLock.lock();
                        if( done ) {
                            break;
                        }
                        stopCond.awaitNanos( SliceReaper.this.getIntervall() * 1000000 );
                    }
                    catch( InterruptedException e ) {
                        Thread.interrupted();
                        break;
                    }
                    finally {
                        stopLock.unlock();
                    }
                }
            }
        });

        worker.setName( "SliceReaper" );
        worker.start();
    }

    @PreDestroy
    public void stop() {
        try {
            stopLock.lock();
            done = true;
            stopCond.signal();
        }
        finally {
            stopLock.unlock();
        }
    }

    public long getIntervall( ) {
        return intervall;
    }

    public void setIntervall( long intervall ) {
        this.intervall = intervall;
    }

    public EntityManagerFactory getEmf( ) {
        return emf;
    }

    @Inject
    public void setEmf( EntityManagerFactory emf ) {
        this.emf = emf;
    }

    public SliceProvider getSliceProvider() {
        return sliceProvider;
    }

    @Inject
    public void setSliceProvider(SliceProvider sliceProvider) {
        this.sliceProvider = sliceProvider;
    }
}
