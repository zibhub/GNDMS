package de.zib.gndms.infra.service;

import de.zib.gndms.infra.system.EMFactoryProvider;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.model.common.GridResource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.apache.axis.types.URI;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * Shared interface of all GNDMS service resource homes
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:51:59
 */
public interface GNDMServiceHome<M extends GridResource>
        extends EMFactoryProvider, SystemHolder, ResourceHome {

    Query getListAllQuery(final @NotNull EntityManager em);

    void refresh(final @NotNull M resourceModel) throws ResourceException;

    @NotNull String getNickName();

    @NotNull Class<M> getModelClass();

    ResourceKey getKeyForResourceModel( GridResource model );
    
    ResourceKey getKeyForId( String id );

	URI getServiceAddress();
}
