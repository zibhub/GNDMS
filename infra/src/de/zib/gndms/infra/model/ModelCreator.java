package de.zib.gndms.infra.model;

import org.jetbrains.annotations.NotNull;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.08.2008 Time: 14:50:41
 */
public interface ModelCreator<M> {
	@NotNull
	M createInitialModel(@NotNull String id, @NotNull String system);
}
