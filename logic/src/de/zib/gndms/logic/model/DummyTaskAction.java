package de.zib.gndms.logic.model;

import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.09.2008 Time: 17:06:35
 */
public class DummyTaskAction extends TaskAction<Task> {
    private static final double SUCCESS_RATE = 0.5;


    @Override
    protected @NotNull Task createInitialTask() {
        return new Task();
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
