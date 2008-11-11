package de.zib.gndms.infra.action;

import com.google.common.collect.Maps;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
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
public class SetupDefaultConfigletAction extends SetupAction<ConfigActionResult> {
	@ConfigOption(descr = "Name of the configlet")
	String name;

	@ConfigOption(descr = "Name of the configlet's class; cant be changed after creation!")
	String className;

	@Override
	public void initialize() {
		super.initialize();    // Overridden method
		try {
			if (name == null && hasOption("name"))
				setName(getOption("name"));
			if (className == null && hasOption("className"))
				setClassName(getOption("className"));
		}
		catch (MandatoryOptionMissingException e) {
			throw new IllegalStateException(e);
		}
		requireParameter("name", name);
		if (SetupMode.CREATE.equals(getMode())) {
			requireParameter("className", className);
		}
	}


	@Override
	public ConfigActionResult execute(
		  final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
		switch (getMode()) {
			case CREATE: return create(em, writer);
			case READ: return read(em, writer);
			case UPDATE: return update(em, writer);
			case DELETE: return delete(em, writer);
			default:
				throw new IllegalStateException("Unexpected SetupMode");
		}
	}


	@SuppressWarnings({ "unchecked" })
	private ConfigActionResult read(final EntityManager emParam, final PrintWriter writerParam) {
		final ConfigletState state = emParam.find(ConfigletState.class, getName());
		if (state == null) return failed("Configlet not found");
		else {
			final Map<String, String> map = (Map<String, String>) state.getState();
			ParameterTools.asString(map, EchoOptionsAction.OPTION_NAME_PATTERN, true);
			return ok();
		}

	}


	private ConfigActionResult create(final EntityManager emParam, final PrintWriter writerParam) {
		if (emParam.find(ConfigletState.class, getName()) != null) return failed("Already exists");
		final ConfigletState state = new ConfigletState();
		state.setName(getName());
		state.setClassName(getClassName());
		update_(state);
		emParam.persist(state);
		return ok();
	}


	private ConfigActionResult update(final EntityManager emParam, final PrintWriter writerParam) {
		ConfigletState state = emParam.find(ConfigletState.class, getName());
		if (state == null) return failed("Configlet not found");
		update_(state);
		return ok();
	}


	private void update_(final ConfigletState stateParam) {
		final @NotNull HashMap<String, String> configMap = Maps.newHashMap();
		for (String optName : getAllOptionNames())
			configMap.put(optName, getOption(optName, ""));
		stateParam.setState(configMap);
	}


	private ConfigActionResult delete(final EntityManager emParam, final PrintWriter writerParam) {
		ConfigletState state = emParam.find(ConfigletState.class, getName());
		if (state == null) return failed("Configlet not found");
		emParam.remove(state);
		return ok();
	}


	@Override
	public boolean isSupportedMode(final SetupMode modeParam) {
		return true;
	}


	public String getName() {
		return name;
	}


	public void setName(final String nameParam) {
		name = nameParam;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(final String classNameParam) {
		className = classNameParam;
	}
}
