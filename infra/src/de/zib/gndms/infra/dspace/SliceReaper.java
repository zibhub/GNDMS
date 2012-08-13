package de.zib.gndms.infra.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.stuff.threading.PeriodicalJob;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SliceReaper extends PeriodicalJob {
    public static final String LIST_ALL_SLICES = "listAllSlices";

    private long period; // in milliseconds
    private boolean done = false;

    protected EntityManagerFactory emf;
    protected SliceProvider sliceProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final @NotNull Lock stopLock = new ReentrantLock();
    private final @NotNull Condition stopCond = stopLock.newCondition();

    @Override
    public String getName() {
        return "SliceReaper";
    }

    protected void call() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( LIST_ALL_SLICES );
        for( Object o : query.getResultList() ) {
            final Slice slice = Slice.class.cast( o );

            if( null == slice ) {
                // this is not happening
                continue;
            }

            // perhaps, slice is too old
            if( slice.getTerminationTime().isBeforeNow() ) {
                if( onSliceTooOld( slice ) )
                    continue;
            }

            try {
                if( sliceProvider.getDiskUsage( slice.getSubspace().getId(), slice.getId() ) > slice.getTotalStorageSize() ) {
                    if( onSliceTooBig( slice ) )
                        continue;
                }
            } catch( NoSuchElementException e ) {
                logger.error( "There seems to be some error in DSpace database.", e );
            }
        }
    }


    private boolean onSliceTooOld( Slice slice ) {
        try {
            sliceProvider.deleteSlice( slice.getSubspace().getId(), slice.getId() );
        } catch (NoSuchElementException e) {
            logger.error( "Could not delete slice " + slice.getId(), e );
        }

        return true;
    }


    private boolean onSliceTooBig( Slice slice ) {
        // TODO: send a mail to system administrator
        return false;
    }


    public Long getPeriod() {
        return period;
    }


    public void setPeriod(long period) {
        this.period = period;
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
