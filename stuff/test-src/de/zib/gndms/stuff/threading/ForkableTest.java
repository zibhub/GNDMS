package de.zib.gndms.stuff.threading;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.*;

/**
 * ForkableTest
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 01.04.11 TIME: 12:47
 */
public class ForkableTest {

    @Test
    public void justFork() throws ExecutionException, InterruptedException {
        Forkable<Integer> forkable = new Forkable<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                while (true) {
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException ie) {
                        // deliberately ignored
                    }
                }
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> integerFuture = executorService.submit(forkable);

        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ie) {

        }
        forkable.setShouldStop(true);
        integerFuture.cancel(true);

        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ie) {

        }
        Assert.assertEquals(forkable.wasStopped(), true);
    }


   @Test
    public void forkAndThrow() throws ExecutionException, InterruptedException {
        Forkable<Integer> forkable = new Forkable<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                int i = 0;
                while (true) {
                    try {
                        if (i == 2)
                            throw new ClassCastException("Whatever");
                        Thread.sleep(100L);
                        i = i + 1;
                    }
                    catch (InterruptedException ie) {
                        // deliberately ignored
                    }
                }
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> integerFuture = executorService.submit(forkable);

        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ie) {

        }

        boolean threwUp = false;
        try {
            integerFuture.get();
        }
        catch (InterruptedException ie) {

        }
        catch (ExecutionException cce) {
            threwUp = true;
        }
        Assert.assertEquals(threwUp, true);
    }}
