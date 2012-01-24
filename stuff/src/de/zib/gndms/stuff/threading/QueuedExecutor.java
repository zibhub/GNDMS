package de.zib.gndms.stuff.threading;

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



import java.util.concurrent.*;
import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 24.02.2009, Time: 14:53:59
 */
public class QueuedExecutor {

    private final Forketeer executor;
    private long defaultDelay  = 0L;
    private long lastTimeStamp = 0L;


    public QueuedExecutor() {
        executor = new Forketeer( defaultDelay );
    }

    public void shutdown() {
        executor.shutdown();
    }


    public List<Runnable> shutdownNow() {
        shutdown( );
        return executor.flush();
    }


    public synchronized <T> DV<T,Exception> submit( final Callable<T> task ) throws InterruptedException {

        final long d = actualDelay( defaultDelay );
        Forketeer.ForkRequest<T> fork =  new Forketeer.ForkRequest<T>( task );
        executor.enqueue( fork );
        return fork.getResult();
    }



    /** Computes an actual timeout for future f, by taking the delay into account.
     *
     *  The actual timeout is the delay plus the <EM>to</EM>. Fi their is no delay for future f  or future f isn't a
     *  scheduledFuture than then <EM>to</EM> to will be returned to.
     * @param f The future, whose timeout should be requested.
     * @param to The desired timeout.
     * @param tu The unit of the timeout
     * @return The sum of timeout and delay.
     */
    public long actualTimeout( Future f, long to, TimeUnit tu ) {

        try {
            ScheduledFuture sf = ScheduledFuture.class.cast( f );
            return sf.getDelay( tu ) + to;
        } catch ( ClassCastException e ) {
            return 0;
        }
    }



    public long getDefaultDelay() {
        return defaultDelay;
    }


    public void setDefaultDelay( final long defaultDelay ) {
        this.defaultDelay = defaultDelay;
        executor.setDelay( defaultDelay );
    }


    private long actualDelay( final long delay ) {
        final long currentTime = System.currentTimeMillis();
        final long currentDelay = currentTime - lastTimeStamp;

        final long newDelay;
        if( currentDelay <= 0 )
            newDelay = StrictMath.abs( currentDelay ) + delay;
        else if( currentDelay < delay )
            newDelay = delay - currentDelay;
        else
            newDelay = 0;

        lastTimeStamp = currentTime + newDelay;
        return newDelay;
    }
}
