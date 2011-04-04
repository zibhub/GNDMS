package de.zib.gndms.stuff.threading;

import  org.jetbrains.annotations.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Forkable
 *
 * A Forkable is a callable that calls another (target) callable in a separate thread.
 *
 * If the forkable's thread is interrupted and shouldStop() has been set to true, the forked
 * target thread will be shutdown using a call to Thread.stop(). Please read the description of Thread.stop
 * to understand what limitations this imposes on the target callable! The intent of this is dealing
 * with legacy libraries which do not properly react to interrupt().
 *
 * If an exception is produced by the target callable's thread, it is delivered in the forkable's thread using
 * Thread.stop(exception), i.e. it is delivered via the UncaughtEceptionHandler.
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 11:55
 */
public class Forkable<T> implements Callable<T> {
    private final @NotNull Callable<T> callable;

    private final @NotNull Lock stopLock = new ReentrantLock();
    private boolean shouldStop = false;

    private final @NotNull Lock resultLock = new ReentrantLock();
    private final @NotNull Condition resultCond = resultLock.newCondition();
    private @NotNull T result;
    private boolean done = false;
    private boolean stopped = false;
    private Exception exception;


    public Forkable(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * @see Forkable
     *
     * @return Result of (target callable).call()
     */
    public T call() {
        final Thread thread = new Thread() {

            public void run() {
                try {
                    final T result = callable.call();
                    resultLock.lock();
                    try {
                        done = true;
                        Forkable.this.result = result;
                        resultCond.signal();
                    }
                    finally {
                        resultLock.unlock();
                    }
                } catch (Exception e) {
                    resultLock.lock();
                    try {
                        done = true;
                        exception = e;
                        resultCond.signal();
                    }
                    finally { resultLock.unlock(); }
                }
            }
        };

        thread.start();

        boolean loop = true;

        while (loop) {
            resultLock.lock();
            try {
                resultCond.await();
                loop = ! done;
            } catch (InterruptedException e) {
                if (shouldStop()) {
                    loop = false;
                    stopped = true;
                    // This is intended!
                    thread.stop();
                }
            }
            finally {
                resultLock.unlock();
            }
        }

        resultLock.lock();
        try {
            if (exception == null)
                return result;
            else
                Thread.currentThread().stop(exception);
        }
        finally { resultLock.unlock();}

        throw new IllegalStateException("Must never be reached");
    }

    public boolean shouldStop() {
        stopLock.lock();
        try {
            return shouldStop;
        }
        finally { stopLock.unlock(); }
    }

    /**
     *
     * @return true if the target callable ran to completion
     */
    public boolean isDone() {
        resultLock.lock();
        try {
            return done;
        }
        finally { resultLock.unlock(); }
    }

    /**
     *
     * @return true if the target callable's thread was stopped
     */
    public boolean wasStopped() {
        resultLock.lock();
        try {
            return stopped;
        }
        finally { resultLock.unlock(); }
    }

    /**
     * If set true, this callable's executing thread will call stop on the target callable's executing thread
     * if it is interrupted
     *
     * @param stopOnInterrupt
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setShouldStop(boolean stopOnInterrupt)    {
        stopLock.lock();
        try {
            shouldStop = stopOnInterrupt;
        }
        finally { stopLock.unlock(); }
    }
}
