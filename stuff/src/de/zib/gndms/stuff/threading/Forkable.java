package de.zib.gndms.stuff.threading;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
 * with legacy JNI code which does not properly react to interrupt().
 *
 * If an exception is produced by the target callable's thread, it is delivered in the forkable's thread using
 * Thread.stop(exception), i.e. it is delivered via the UncaughtEceptionHandler.
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 11:55
 */
@SuppressWarnings({"UnusedDeclaration"})
public class Forkable<T> implements Callable<T> {

    public static final long DEFAULT_WAKEUP = 4000L;

    private final @NotNull Callable<T> callable;

    private final @NotNull Lock stopLock = new ReentrantLock();
    private boolean shouldStop = false;

    private final @NotNull Lock resultLock = new ReentrantLock();
    private final @NotNull Condition resultCond = resultLock.newCondition();
    private @NotNull T result;
    private boolean start = true;
    private boolean done = false;
    private boolean stopped = false;
    private Exception exception;

    private final boolean unlimitedDuration;
    private final long wakeup;
    private final TimeUnit wakeupUnit;
    private final long deadline;


    /**
     * Constructs a forkable for running callable until it finishes or setShoudlStop(true) is called concurrently
     *
     * @param callable the inner (target) callable to be run by this forkable
     */
    public Forkable(@NotNull Callable<T> callable) {
        this.callable   = callable;
        this.unlimitedDuration = true;
        this.deadline   = 0L;
        this.wakeup     = 0L;
        this.wakeupUnit = null;
    }


    /**
     * Constructs a forkable for running callable until it finishes or setShoudlStop(true) is called concurrently.
     * The callable may be stopped after deadline has been reached via Thread.stop. This is checked every
     * wakeup wakeupUnit time units.
     *
     * @param callable the inner (target) callable to be run by this forkable
     * @param deadline  after which the callable will be stopped forcefully
     * @param wakeup checking delay amount
     * @param wakeupUnit checking delay time unit
     */
    public Forkable(@NotNull Callable<T> callable, long deadline, long wakeup, @NotNull TimeUnit wakeupUnit) {
        this.callable   = callable;
        this.wakeup     = wakeup;
        this.wakeupUnit = wakeupUnit;
        this.deadline   = deadline;
        this.unlimitedDuration = false;
    }


    /**
     * Constructs a forkable for running callable until it finishes or setShoudlStop(true) is called concurrently.
     * The callable may be stopped after deadline has been reached via Thread.stop. This is checked every
     * DEFAULT_WAKEUP milliseconds.
     *
     * @param callable the inner (target) callable to be run by this forkable
     * @param deadline  after which the callable will be stopped forcefully
     */
    public Forkable(@NotNull Callable<T> callable, long deadline) {
        this(callable, deadline, DEFAULT_WAKEUP, TimeUnit.MILLISECONDS);
    }


    /**
     * @see Forkable
     *
     * @return Result of (target callable).call()
     */
    @SuppressWarnings({"deprecation"})
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

        boolean start   = true;
        boolean loop    = true;
        boolean timeOut = false;

        while (loop) {
            resultLock.lock();
            if (start) {
                thread.start();
                start = false;
            }

            try {
                if (unlimitedDuration)
                    resultCond.await();
                else {
                    if (System.currentTimeMillis() >= deadline) {
                        setShouldStop(true);
                        timeOut = true;
                        throw new InterruptedException();
                    }
                    else
                        resultCond.await(wakeup, wakeupUnit);
                }
                loop = ! done;
            } catch (InterruptedException e) {
                Thread.interrupted();
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
            if (timeOut)
                Thread.currentThread().stop(new TimeoutException("Deadline hit"));
            else {
                if (exception == null)
                    return result;
                else
                    Thread.currentThread().stop(exception);
            }
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
     * as soon as it is interrupted
     *
     * @param stopOnInterrupt the new value for should stop
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setShouldStop(boolean stopOnInterrupt)    {
        stopLock.lock();
        try {
            shouldStop = stopOnInterrupt;
        }
        finally { stopLock.unlock(); }
    }

    /**
     *
     * @return true if this forkable will use non-timeouted waits and is not terminated based on a deadline
     */
    public boolean hasUnlimitedDuration() {
        return unlimitedDuration;
    }

    /**
     * @return time amount used by waits
     * @throws IllegalStateException iff hasUnlimitedDuration()
     */
    public long getWakeup() throws IllegalStateException {
        if (unlimitedDuration)
            throw new IllegalStateException("unlimitedDuration");
        else
            return wakeup;
    }

    /**
     * @return time unit used by waits
     * @throws IllegalStateException iff hasUnlimitedDuration()
     */
    public @NotNull TimeUnit getWakeupUnit() throws IllegalStateException  {
        if (unlimitedDuration)
            throw new IllegalStateException("unlimitedDuration");
        else
            return wakeupUnit;
    }

    /**
     * @return deadline after which the target callable will be actively terminated
     * @throws IllegalStateException iff hasUnlimitedDuration()
     */
    public long getDeadline() throws IllegalStateException  {
        if (unlimitedDuration)
            throw new IllegalStateException("unlimitedDuration");
        else
            return deadline;
    }
}
