package de.zib.gndms.infra.access;

import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.ResourceException;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.model.common.GridResource;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:58:43
 */
public interface ServiceHomeProvider {
	void addHome(@NotNull GNDMServiceHome home)
            throws ResourceException;

	@SuppressWarnings({ "unchecked" })
	<M extends GridResource> GNDMPersistentServiceHome<M>
	getHome(Class<M> modelClazz);

	GNDMServiceHome lookupServiceHome(@NotNull String instancePrefix);

	@SuppressWarnings({ "HardcodedFileSeparator", "RawUseOfParameterizedType" })
	<K extends GridResource> void addHome(
	        Class<K> modelClazz, @NotNull GNDMServiceHome home)
	        throws ResourceException;
}
