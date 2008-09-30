package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

//import org.jetbrains.annotations.NotNull;


public enum TaskState {
    CREATED(false),
    CREATED_UNKNOWN(true),
    INITIALIZED(false),
    INITIALIZED_UNKNOWN(true),
    IN_PROGRESS(false),
    IN_PROGRESS_UNKNOWN(true),
    FAILED(false),
    FINISHED(false);

    final boolean unknownState;


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