package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 12:30:22
 */
public class ProviderStageInActionFactory
	  extends InjectingRecursiveKeyFactory<OfferType, ORQTaskAction<?>> {
	
    @Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull MapConfig config = new MapConfig(keyParam.getConfigMap());
	    final Class<? extends AbstractProviderStageInAction> instanceClass = config.getClassOption(
		      AbstractProviderStageInAction.class, "stagingClass",
		      ExternalProviderStageInAction.class);
	    final AbstractProviderStageInAction newInstance = instanceClass.newInstance();
	    injectMembers(newInstance);
	    return newInstance;
    }
}
