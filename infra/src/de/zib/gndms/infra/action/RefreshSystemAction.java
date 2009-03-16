package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * Reloads current <tt>Configlet</tt> to the GNDMSystem.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 07.11.2008 Time: 15:19:18
 */
@ConfigActionHelp(shortHelp = "Refresh system state from database (Currently: Configlets)")
public class RefreshSystemAction extends SystemAction<ConfigActionResult> {
	@Override
	public ConfigActionResult execute(
		  final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
		getSystem().reloadConfiglets();
		return ok();
	}
}
