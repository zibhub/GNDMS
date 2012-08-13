/*
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

package de.zib.gndms.stuff.threading;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date: 07.08.12
 * @time: 13:52
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class PeriodicalJob {

    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    private boolean done = false;
    private final @NotNull Lock stopLock = new ReentrantLock();
    private final @NotNull Condition stopCond = stopLock.newCondition();


    /**
     * Get the period of job repetition.
     * @return Time to wait until next execution of job.
     */
    public abstract Long getPeriod();


    /**
     * Get the name of the thread.
     * @return Some string to identify the thread
     */
    public String getName() {
        return UUID.randomUUID().toString();
    }


    /**
     * Do the periodical work.
     */
    protected abstract void call() throws Exception;


    @PostConstruct
    public void start() {
        Thread worker = new Thread( new Runnable() {
            @Override
            public void run() {
                while( !done ) {
                    try {
                        stopLock.lock();
                        stopCond.awaitNanos( getPeriod() * 1000000 );
                    }
                    catch( InterruptedException e ) {
                        Thread.interrupted();
                        break;
                    }
                    finally {
                        stopLock.unlock();
                    }

                    try {
                        PeriodicalJob.this.call();
                    }
                    catch( Exception e ) {
                        logger.error( "Could not run job.", e );
                    }
                }
            }
        });

        worker.setName( getName() );
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
}
