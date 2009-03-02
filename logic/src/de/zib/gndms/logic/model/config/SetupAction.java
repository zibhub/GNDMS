package de.zib.gndms.logic.model.config;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;


/**
 * Setup actions are actions that know whether they should create a new object or update
 * an existing one.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 10:56:41
 */
public abstract class SetupAction<R> extends ConfigAction<R> {
    public enum SetupMode { CREATE, READ, UPDATE, DELETE }

    @ConfigOption(descr = "Action mode; one of create, read, update, or delete")
    SetupMode mode;


    /**
     * Calls {@code super.initalize()} and tries to set the setup mode.
     * If no setup mode has been denoted, it will try to retrieve it by looking up the
     * configuration map (ConfigAction's {@code  cmdParam} map).
     * 
     * @see ConfigAction#initialize() 
     */
    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (mode == null && hasOption("mode"))
            try {
	            final SetupMode modeOption = getEnumOption(SetupMode.class, "mode", true);
	            setMode(modeOption);

            }
            catch (MandatoryOptionMissingException e) {
                throw new IllegalStateException("Invalid mode option specified", e);
            }
	    final SetupMode setupMode = getMode();
	    if (setupMode == null)
            throw new IllegalStateException("No mode specified");
	    if (! isSupportedMode(setupMode))
		    throw new IllegalStateException("Unsupported mode: " + setupMode.toString());

    }

    /**
     * Returns the setup mode. If no mode has been set it will try to return a parent of this,
     * being a {@code SetupAction} and having setup mode properly set.
     * 
     * @return the setup mode of this or one of its parents.
     */
    @SuppressWarnings({ "NonBooleanMethodNameMayNotStartWithQuestion" })
    public SetupMode getMode() {
        if (mode == null) {
            SetupAction<?> action = nextParentOfType(SetupAction.class);
            return action == null ? null : action.getMode();
        }
        return mode;
    }


    /**
     * Returns true if {@code modeParam} is not in Read mode.
     * @param modeParam
     * @return true if {@code modeParam} is not in Read mode.
     */
	@SuppressWarnings({ "MethodMayBeStatic" })
	public boolean isSupportedMode(SetupMode modeParam) {
		return !SetupMode.READ.equals(modeParam);
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

    public final boolean isDeleting() {
        return SetupMode.DELETE.equals(getMode());
    }

	public final boolean isReading() {
		return SetupMode.READ.equals(getMode());
	}
}
