package de.zib.gndms.infra.service;

import de.zib.gndms.infra.system.EMFactoryProvider;
import de.zib.gndms.infra.system.SystemHolder;
import org.apache.axis.types.URI;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;


/**
 * Shared interface of all GNDMS service resource homes
 *
 * @author Stefan Plantikow<plantikow@zib.de>
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
