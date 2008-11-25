package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.factory.AbstractRecursiveFactory;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInORQFactory extends AbstractRecursiveFactory<OfferType, AbstractORQCalculator<?,?>> {

    @Override
    @NotNull
    public AbstractProviderStageInORQCalculator newInstance(@NotNull final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull MapConfig config = new MapConfig(keyParam.getConfigMap());
        return config.getClassOption(AbstractProviderStageInORQCalculator.class, "estimationClass", ExternalProviderStageInORQCalculator.class).newInstance();
    }
}
