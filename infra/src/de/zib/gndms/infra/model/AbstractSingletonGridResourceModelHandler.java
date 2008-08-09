package de.zib.gndms.infra.model;

import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.SingletonGridResource;


/**
 * This helper class is inserted here in the inheritance chain to solve a groovy compiler bug
 * regarding generics and inheritance 
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.08.2008 Time: 13:22:26
 */
public class AbstractSingletonGridResourceModelHandler<M extends SingletonGridResource, H extends GNDMServiceHome, R extends ReloadablePersistentResource<M, H>> extends GridResourceModelHandler<M, H, R> {

	public AbstractSingletonGridResourceModelHandler(final Class<M> theClazz, final H homeParam) {
		super(theClazz, homeParam);
	}
}
