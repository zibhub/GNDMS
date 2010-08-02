package de.zib.gndms.infra.service;

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



import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.kit.access.EMFactoryProvider;
import org.apache.axis.types.URI;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;


/**
 * Shared interface of all GNDMS service resource homes
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:51:59
 */
public interface GNDMServiceHome
        extends EMFactoryProvider, SystemHolder, ResourceHome {


    @NotNull String getNickName();

    @NotNull URI getServiceAddress();

    ResourceKey getKeyForId( @NotNull String id );


}
