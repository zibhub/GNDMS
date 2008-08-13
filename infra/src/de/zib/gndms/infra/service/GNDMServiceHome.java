package de.zib.gndms.infra.service;

import de.zib.gndms.infra.db.EMFactoryProvider;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;


/**
 * Shared interface of all GNDMS service resource homes
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:51:59
 */
public interface GNDMServiceHome<M extends GridResource>
        extends ServiceInfo, EMFactoryProvider, SystemHolder {

    void refresh(final @NotNull GridResource resource);

    @NotNull String getNickName();

    @NotNull Class<M> getModelClass();
}
