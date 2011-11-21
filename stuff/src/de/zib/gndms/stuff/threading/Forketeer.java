package de.zib.gndms.stuff.threading;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.stuff.threading.impl.DefaultDV;

import java.util.List;
import java.util.concurrent.*;

/**
 * Forketeer
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 16:39
 */
public class Forketeer {
    private long delay;

    private boolean run = true;
    private final Thread thread;
    private final BlockingQueue<ForkRequest> q = new LinkedBlockingQueue<ForkRequest>();


    public Forketeer(long delay) {
        this.delay = delay;

        thread = new Thread(new Runnable() {
            public void run() {
                while( run ) {
                    try {
                        final ForkRequest request = q.take();
                        final Thread forkOff = new Thread(request);
                        forkOff.start();
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                    if( run )
                        waitUntil(System.currentTimeMillis() + getDelay());
                }
            }
        });

        thread.start();
    }

    public void enqueue(final @NotNull ForkRequest req) throws InterruptedException {
        q.put(req);
    }


    public <T> ForkRequest<T> enqueue( final @NotNull Callable<T> req) throws InterruptedException {
        ForkRequest<T> freq = new ForkRequest<T>( req );
        q.put( freq );
        return freq;
    }


    private void waitUntil(long t) {
        for(long gap = t-System.currentTimeMillis(); gap > 0L; gap = t-System.currentTimeMillis())
            try {
                Thread.sleep(gap);
            }
            catch (InterruptedException ie) {
                Thread.interrupted();
            }
    }


    /**
     * @brief Graceful shutdown, waits for current thread to finish.
     */
    public void shutdown( ) {
        run = false;
    }


    /**
     * @brief Shutdown, the executor thread, by stopping it.
     */
    public void shutdownNow( ) {
        thread.stop();
    }


    public synchronized long getDelay() {
        return delay;
    }

    public synchronized void setDelay(long delay) {
        this.delay = delay;
    }


    // implementation should empty the request queue and return a list of runnables for each
    // element in the queue.
    public List<Runnable> flush() {
        return null;  // Implement Me. Pretty Please!!!
    }


    public static class ForkRequest<T> implements Runnable {
        private final Callable<T> callable;
        private final DV<T, Exception> result = DefaultDV.Factory.getInstance().newEmpty();

        public ForkRequest(@NotNull Callable<T> callable) {
            this.callable = callable;
        }

        public void run() {
            try {
                result.setValue(callable.call());
            } catch (Exception e) {
                result.setCause(e);
            }
        }

        public DV<T, Exception> getResult() {
            return result;
        }
    }
}
