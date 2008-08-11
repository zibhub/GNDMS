package de.zib.gndms.infra.model;

import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.GridResource;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 11.08.2008 Time: 13:50:15
 */
public abstract class AbstractGridResourceModelHandler<M extends GridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridEntityModelHandler<M, H, R> {

	public AbstractGridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);
	}
}
