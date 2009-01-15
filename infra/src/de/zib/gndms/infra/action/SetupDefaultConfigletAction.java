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
 * ThingAMagic.
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


    @Override
	protected ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
		update_(state);
        return ok();
	}


    @Override
	protected ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam) {
		update_(state);
        return ok();
	}


	private void update_(final ConfigletState stateParam) {
		final @NotNull HashMap<String, String> configMap = Maps.newHashMap();
		for (String optName : getAllOptionNames())
			configMap.put(optName, getOption(optName, ""));
		stateParam.setState(configMap);
	}
}
