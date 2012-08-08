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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date: 07.08.12
 * @time: 13:52
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class PeriodcialJob extends Thread {

    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    final static int listenPeriod = 250;
    private boolean run = true;


    public void finish() {
        run = false;
        try {
            this.join();
        } catch( InterruptedException e ) {
        }
    }


    /**
     * Get the period of job repetition.
     * @return Time to wait until next execution of job.
     */
    public abstract Integer getPeriod();


    /**
     * Do the periodical work.
     */
    public abstract void call() throws Exception;


    @Override
    public void run() {
        while( run ) {
            try {
                final Integer period = getPeriod();
                
                int i = 0;
                
                while( run && i < period ) {
                    final int d = period-i < listenPeriod ? period-i : listenPeriod;
                    
                    i += d;
                    Thread.sleep( d );
                }
            } catch( InterruptedException e ) {
                run = false;
                return;
            }

            try {
                call();
            }
            catch( Exception e ) {
                logger.error( "Could not run job.", e );
            }
        }
    }
}
