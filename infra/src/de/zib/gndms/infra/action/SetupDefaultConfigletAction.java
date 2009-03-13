package de.zib.gndms.infra.action;

import com.google.common.collect.Maps;
import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.model.config.*;
import de.zib.gndms.model.common.ConfigletState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * This class provides a default implementation of {@code SetupConfigletAction}, intended to store and manage
 * all option names and their current chosen values of the configuration map in the database.
 *
 * <p>When this action is started with <tt>create</tt> as SetupMode, the configuration map must
 * have an option 'className' set. Otherwise an <tt>IllegalStateException</tt> will be thrown.
 *
 *
 * <p>An instance of {@code SetupPermissionConfigletAction} manages entities, being an instance of {@code ConfigletState}.
 * If SetupMode ist set to <tt>create</tt> or <tt>update</tt>, the <tt>ConfigletState</tt> entity will be overwritten by a new map,
 * containing all options and their chosen values in the current configuration.
 *
 * <p>The current configuration will be written to a given <tt>PrintWriter</tt>, if SetupMode is <tt>read</tt>.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.11.2008 Time: 15:06:35
 */
@ConfigActionHelp(shortHelp = "Setup configlets in the database")
public class SetupDefaultConfigletAction extends SetupConfigletAction {

	@Override
	public void initialize() {
		super.initialize();    // Overridden method

		requireParameter("name", getName());
		if (SetupMode.CREATE.equals(getMode())) {
			requireParameter("className", getClassName());
		}
	}



    @Override
    @SuppressWarnings({ "unchecked" })
	protected ConfigActionResult read( final ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
        final Map<String, String> map = (Map<String, String>) state.getState();
        writerParam.printf( ParameterTools.asString(map, EchoOptionsAction.OPTION_NAME_PATTERN, true) );
        return ok();
	}


    /**
     * Creates a new Map containing all option names and their corresponding value set in the current configuration
     *  and sets the state of {@code stateParam} to the map.
     *
     * @param state the ConfigletState to be created
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam
     * @return Return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @Override
	protected ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
		update_(state);
        return ok();
	}

    /**
     *  Creates a new Map containing all global option names and their corresponding value set in the current configuration
     *  and sets the state of {@code stateParam} to the map.
     * 
     * @param state the ConfigletState to be updated
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @Override
	protected ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
		update_(state);
        return ok();
	}

    /**
     * Creates a new Map containing all option names and their corresponding value set in the current configuration
     *  and sets the state of {@code stateParam} to the map.
     *
     * @param stateParam the ConfigletState to be updated
     */
	private void update_(final ConfigletState stateParam) {
		final @NotNull HashMap<String, String> configMap = Maps.newHashMap();
		for (String optName : getAllOptionNames())
			configMap.put(optName, getOption(optName, ""));
		stateParam.setState(configMap);
	}
}
