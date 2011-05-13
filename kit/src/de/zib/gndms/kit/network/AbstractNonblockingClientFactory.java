package de.zib.gndms.kit.network;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author try ma ik jo rr a zib
 * @date 05.04.11  12:35
 * @brief
 */
public abstract class AbstractNonblockingClientFactory extends AbstractGridFTPClientFactory implements Schedulable{

    protected final Logger log = Logger.getLogger( this.getClass() );
    private int timeout = 20;
    private long delay = 500; // in ms
    private int count=0;
    private final TimeUnit unit = TimeUnit.SECONDS;


    protected synchronized int inc() {
        return ++count;
    }


    public void shutdown() {

    }


    public int getTimeout() {
        return timeout;
    }


    public void setTimeout( int timeout ) {

        if( timeout < 0 )
            throw new IllegalArgumentException( "Timeout must be greater or equal 0" );


        this.timeout = timeout;
        log.info( "updated timeout to: "+ timeout );
    }


    public long getDelay() {
        return delay;
    }


    public void setDelay( int delay ) {

        if( delay < 0 )
            throw new IllegalArgumentException( "Delay must be greater or equal 0" );

        this.delay = delay;
        log.info( "updated delay to: "+ delay );
    }


    public TimeUnit getUnit() {
         return unit;
     }
}
