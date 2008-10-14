package de.zib.gndms.infra.model;

import org.jetbrains.annotations.NotNull;


/**
 * Instances initialize models by doing nothing
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 12:40:29
 */
public class DefaultModelInitializer<M> implements ModelInitializer<M> {
	public void initializeModel(final @NotNull M model) {
		// intended
	}
}
