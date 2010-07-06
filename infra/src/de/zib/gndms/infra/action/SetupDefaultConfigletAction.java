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
 * This class provides a default implementation of {@code SetupConfigletAction}, intended to manage
 * all option names and their chosen values,stored in a <tt>ConfigletState</tt> entity.
 *
 * <p>If {@link #mode} ist set to <tt>create</tt> or <tt>update</tt>,
 * the state of the <tt>ConfigletState</tt> entity will be overwritten by a new map,
 * containing all options and their chosen values of the current configuration.
 *
 * <p>When this action is started with <tt>create</tt> as chosen SetupMode, the configuration map must
 * have an option {@link #className 'className'} set. Otherwise an <tt>IllegalStateException</tt> will be thrown.
 * The option {@link #name 'name'} must be set in any case.
 * 
 * <p>The current configuration will be written to a given <tt>PrintWriter</tt>, if {@code mode} is set to <tt>read</tt>.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the <tt>execute()</tt> method is called.
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


    /**                                      
     * Prints all option names and their chosen values of the Configlet entity using the PrintWriter {@code writerParam}
     * and the syntax as described in {@link de.zib.gndms.kit.config.ParameterTools#asString(java.util.Map, java.util.regex.Pattern, boolean)}
     *
     * @param state a ConfigletState containing the option names and their corresponding values
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam the printwriter the state will be written to.
     * @return An {@code OKResult} instance
     */
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
     * @return An {@code OKResult} instance
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
     * @return An {@code OKResult} instance,
     */
    @Override
	protected ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
		update_(state);
        return ok();
	}

    /**
     * Creates a new Map containing all option names and their corresponding value set in the current configuration
     *  and sets the state of {@code stateParam} to the created map.
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
