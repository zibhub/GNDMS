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
import java.util.Collection;
import java.util.ArrayList;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 24.02.2009, Time: 14:53:59
 */
public class QueuedExecutor implements ExecutorService {

    private final ScheduledExecutorService executor;
    private long defaultDelay  = 0L;
    private long lastTimeStamp = 0L;


    public QueuedExecutor() {
        executor = Executors.newScheduledThreadPool( 1 );
    }


    public QueuedExecutor( int coreSize ) {
        executor = Executors.newScheduledThreadPool( coreSize );
    }


    public QueuedExecutor( ScheduledExecutorService sharedPool ) {
        executor = sharedPool;
    }


    public void shutdown() {
        executor.shutdown( );
    }


    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }


    public boolean isShutdown() {
        return executor.isShutdown();
    }


    public boolean isTerminated() {
        return executor.isTerminated();
    }


    public boolean awaitTermination( final long timeout, final TimeUnit unit ) throws InterruptedException {
        return executor.awaitTermination( timeout, unit );
    }


    public synchronized <T> Future<T> submit( final Callable<T> task ) {

        final long d = actualDelay( defaultDelay );
        return executor.schedule( task, d, TimeUnit.MILLISECONDS );
    }


    public synchronized <T> Future<T> submit( final Runnable task, final T result ) {
        final long d = actualDelay( defaultDelay );
        return (Future<T>) executor.schedule( task, d, TimeUnit.MILLISECONDS );
    }


    public synchronized Future<?> submit( final Runnable task ) {
        final long d = actualDelay( defaultDelay );
        return executor.schedule( task, d, TimeUnit.MILLISECONDS );
    }


    public List invokeAll( final Collection tasks ) throws InterruptedException {
        final List res = submitList( tasks );

        for( Object o :  res ) {
						Future f = (Future) o;
            try {
                f.get();
            } catch ( ExecutionException e ) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return res;
    }


    public List invokeAll( final Collection tasks, final long timeout, final TimeUnit unit ) throws InterruptedException {
        final List res = submitList( tasks );

        unit.sleep( timeout );
        for( Object o :  res ) {
            final Future f = (Future) o;
            if(! f.isDone() )
                f.cancel( true );
        }

        return res;
    }


    public Object invokeAny( final Collection tasks ) throws InterruptedException, ExecutionException {

        final List l = submitList( tasks );
        Object res = null;

        for( Object o :  l ) {
            try {
	            Future f = (Future) o;
                res = f.get();
            } catch ( ExecutionException e ) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return res;
    }


    public Object invokeAny( final Collection tasks, final long timeout, final TimeUnit unit ) throws InterruptedException, ExecutionException, TimeoutException {

        final List l = submitList( tasks );
        Object res = null;

        unit.sleep( timeout );
        for( Object o : l ) {
            Future f = (Future) o;
            if(! f.isDone() )
                f.cancel( true );
            else
                res = f.get( );
        }

        return res;
    }


    public void execute( final Runnable command ) {
        executor.execute( command );
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
    }


    private <T> List<Future<T>> submitList( final Collection<Callable<T>> tasks ) {
        List<Future<T>> res = new ArrayList<Future<T>>();
        for( Callable<T> ct: tasks )
            res.add( submit( ct ) );

        return res;
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
