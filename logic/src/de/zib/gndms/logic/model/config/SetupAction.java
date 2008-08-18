package de.zib.gndms.logic.model.config;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;


/**
 * Setup actions are actions that know wether they should create a new object or update
 * an existing one.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 10:56:41
 */
public abstract class SetupAction<R> extends ConfigAction<R> {
    public enum SetupMode { CREATE, UPDATE }

    SetupMode mode;


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (mode == null && hasOption("mode"))
            try {
                setMode(getLCEnumOption(SetupMode.class, "mode"));
            }
            catch (MandatoryOptionMissingException e) {
                throw new IllegalStateException("Invalid mode option specified", e);
            }
        if (getMode() == null)
            throw new IllegalStateException("No mode specified");

    }


    @SuppressWarnings({ "NonBooleanMethodNameMayNotStartWithQuestion" })
    public SetupMode getMode() {
        if (mode == null) {
            SetupAction<?> action = nextParentOfType(SetupAction.class);
            return action == null ? null : action.getMode();
        }
        return mode;
    }


    public void setMode(final SetupMode modeParam) {
        mode = modeParam;
    }

    public final boolean isCreating() {
        return SetupMode.CREATE.equals(getMode());
    }

    public final boolean isUpdating() {
        return SetupMode.UPDATE.equals(getMode());
    }
}
