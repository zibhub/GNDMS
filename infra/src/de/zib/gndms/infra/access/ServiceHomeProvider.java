package de.zib.gndms.infra.access;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.ResourceException;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.model.common.GridResource;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
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
	<K extends GridResourceItf> void addHome(
	        Class<K> modelClazz, @NotNull GNDMServiceHome home)
	        throws ResourceException;
}
