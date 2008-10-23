package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * Dummy action that finishes with probability SUCCESS_RATE.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:06:35
 */
@SuppressWarnings({ "FeatureEnvy" })
public class DummyTaskAction extends TaskAction {
    private double successRate = 1.0d;
    private long sleepInProgress;


    public DummyTaskAction() {
        super();
    }


    public DummyTaskAction(final @NotNull EntityManager em, final @NotNull Task model) {
        super(em, model);
    }


    public DummyTaskAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
    }



    @Override
    protected @NotNull Class<Task> getTaskClass() {
        return Task.class;
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onInProgress(final @NotNull Task model) {
        try {
            Thread.sleep(sleepInProgress);
        }
        catch (InterruptedException e) {
            // onward!
            shutdownIfTerminating(e);
        }
        if (Math.random() < successRate)
            finish(1);
         else
            fail(new IllegalStateException("Random failure"));
    }


    public double getSuccessRate() {
        return successRate;
    }


    public long getSleepInProgress() {
        return sleepInProgress;
    }


    public void setSuccessRate(final double successRateParam) {
        if (successRateParam < 0.0d)
            throw new IllegalArgumentException("succesRate must not be < 0.0d");
        if (successRateParam > 1.0d)
            throw new IllegalArgumentException("succesRate must not be > 1.0d");
        successRate = successRateParam;
    }


    public void setSleepInProgress(final long sleepInProgressParam) {
        if (sleepInProgressParam < 0L)
            throw new IllegalArgumentException("sleepInProgress must be >= 0L");
        sleepInProgress = sleepInProgressParam;
    }
}
