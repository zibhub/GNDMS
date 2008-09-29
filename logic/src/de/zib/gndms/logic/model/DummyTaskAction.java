package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.UUID;


/**
 * Dummy action that finishes with probability SUCCESS_RATE.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:06:35
 */
public class DummyTaskAction extends TaskAction<Task> {
    private static final double SUCCESS_RATE = 0.5;


    public DummyTaskAction(final @NotNull UUID pk) {
        super(pk);
    }


    public DummyTaskAction() {
        super();
    }


    @Override
    protected @NotNull Task createInitialTask() {
        final Task task = new Task();
        Contract contract = new Contract();
        DateTime dt = new DateTime().toDateTimeISO();
        contract.setAccepted(dt.toGregorianCalendar());
        contract.setDeadline(dt.plusYears(2).toGregorianCalendar());
        contract.setResultValidity(dt.plusYears(2).toGregorianCalendar());
        task.setDescription("Dummy");
        task.setTerminationTime(contract.getResultValidity());
        task.setType(null);
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
        if (Math.random() < SUCCESS_RATE)
            finish(1);
         else
            fail(new IllegalStateException("Random failure"));
    }
}
