package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;


/**
 * Dummy action that finishes with probability SUCCESS_RATE.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:06:35
 */
public class DummyTaskAction extends TaskAction<Task> {
    private double successRate = 1.0d;
    private long sleepInProgress = 0;

    public DummyTaskAction(final @NotNull EntityManager em, final @NotNull Object pk) {
        super(em, pk);
    }


    public DummyTaskAction() {
        super();
    }


    @Override
    protected @NotNull Task createInitialTask() {
        final Task task = new Task();
        task.setId(nextUUID());
        Contract contract = new Contract();
        DateTime dt = new DateTime().toDateTimeISO();
        contract.setAccepted(dt.toGregorianCalendar());
        contract.setDeadline(dt.plusYears(2).toGregorianCalendar());
        contract.setResultValidity(dt.plusYears(2).toGregorianCalendar());
        task.setDescription("Dummy");
        task.setTerminationTime(contract.getResultValidity());
        task.setOfferType(null);
        task.setContract(contract);
        return task;
    }


    @Override
    protected @NotNull Class<Task> getTaskClass() {
        return Task.class;
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onInProgress(final @NotNull Task model) {
        long helper = 0;
        try {
            /* to get a clean breakpoint */
            helper++;
            Thread.sleep(sleepInProgress + (helper - 1L));
        }
        catch (InterruptedException e) {
            // onward!
            wrapInterrupt_(e);
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
        if (sleepInProgressParam < 0)
            throw new IllegalArgumentException("sleepInProgress must be >= 0");
        sleepInProgress = sleepInProgressParam;
    }
}
