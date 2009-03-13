package de.zib.gndms.logic.model.config;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;


/**
 * A {@code SetupActions} extends a {@link ConfigAction} by a setup mode flag.
 * The setup mode must be either
 * <ul>
 *       <li> create </li>
 *       <li> read </li>
 *       <li> update </li>
 *       <li> delete </li>
 * </ul>
 * and may be used during {@code initialize()},{@code execute()} and {@code cleanUp()}.
 *
 * <p>The template parameter R is the type of the result that is computed by the action.
 *
 * <p>When {@code initialize()} is invoked, it will try to retrieve the setup mode.
 * If no value has been set yet, it looks up the option 'mode' from the configuration map.
 * If nothing denoted, it tries to retrieve the setup mode from its parent chain.
 *
 * By default, read mode is not supported. If a subclass shall support this mode, overwrite
 * {@link #isSupportedMode(de.zib.gndms.logic.model.config.SetupAction.SetupMode)} }.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 10:56:41
 */
public abstract class SetupAction<R> extends ConfigAction<R> {

    /**
     * A SetupAction can create, read, update and delete its state
     */
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
     * Returns the setup mode. If no mode has been set, it looks up the parent chain for an instance,
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
     * Returns true if {@code modeParam} is not in <tt>read</tt> mode.
     *
     * @param modeParam
     * @return true if {@code modeParam} is not in <tt>read</tt> mode.
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
