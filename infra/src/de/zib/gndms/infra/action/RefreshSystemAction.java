package de.zib.gndms.infra.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * Reloads current <tt>Configlets</tt> to the GNDMSystem.
 *
 * @see de.zib.gndms.kit.configlet.Configlet
 * @see de.zib.gndms.model.common.ConfigletState
 * @see de.zib.gndms.infra.system.GNDMSystem
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
