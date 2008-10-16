package de.zib.gndms.infra.model;

import org.jetbrains.annotations.NotNull;

/**
 * Implementors initialize model objects
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.08.2008 Time: 14:50:41
 */
public interface ModelInitializer<M> {
	void initializeModel(final @NotNull M model);
}