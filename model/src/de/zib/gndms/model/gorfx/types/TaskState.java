package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import de.zib.gndms.model.gorfx.AbstractTask;              
//import org.jetbrains.annotations.NotNull;


/**
 * A TaskState is an enum holding all possible states of a task. It is used in conjunction with an {@link AbstractTask}.
 * A TaskState can be either
 * <ul>
 *      <li> CREATED </li>
 *      <li>CREATED_UNKNOWN </li>
 *      <li> INITIALIZED  </li>
 *      <li>INITIALIZED_UNKNOWN</li>
 *      <li> IN_PROGRESS  </li>
 *      <li> IN_PROGRESS_UNKNOWN </li>
 *      <li> FAILED </li>
 *      <li> FINISHED </li>
 * </ul>
 *
 * A state can only be changed to a state beneath it, using the order given above.
 * Using the method {@link #transit(TaskState)},
 *  it can be checked if the desired transition is valid.
 *
 */
public enum TaskState implements Serializable {
    CREATED(false),
    CREATED_UNKNOWN(true),
    INITIALIZED(false),
    INITIALIZED_UNKNOWN(true),
    IN_PROGRESS(false),
    IN_PROGRESS_UNKNOWN(true),
    FAILED(false),
    FINISHED(false);

    final boolean unknownState;



    /**
     * Checks if a transition from the state of {@code this} to the state of {@code to} is allowed.
     * If it is not possible, an IllegalStateException will be thrown. Otherwise {@code to} is returned.
     * 
     * @param to a TaskState, {@code this} should be changed to.
     * @return {@code to}, if the state of {@code this} can be changed to the state of {@code to}
     */
    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod" })
    public TaskState transit(final @NotNull TaskState to) {
        switch(this) {
            case CREATED:
            case CREATED_UNKNOWN:
                if (CREATED.equals(to)) return to;
                if (CREATED_UNKNOWN.equals(to)) return to;

            case INITIALIZED:
            case INITIALIZED_UNKNOWN:
                if (INITIALIZED.equals(to)) return to;
                if (INITIALIZED_UNKNOWN.equals(to)) return to;

            case IN_PROGRESS:
            case IN_PROGRESS_UNKNOWN:
                if (IN_PROGRESS.equals(to)) return to;
                if (IN_PROGRESS_UNKNOWN.equals(to)) return to;

                if (FINISHED.equals(to)) return to;

           case FAILED:
                if (FAILED.equals(to)) return to;
                break;

            case FINISHED:
                if (FINISHED.equals(to)) return to;
                break;

           default:
                // intentionally
        }
       throw new IllegalStateException("Invalid TaskState transition");
    }

    
    TaskState(final boolean unknownStateParam) {
        unknownState = unknownStateParam;
    }


    public boolean isUnknownState() {
        return unknownState;
    }
}