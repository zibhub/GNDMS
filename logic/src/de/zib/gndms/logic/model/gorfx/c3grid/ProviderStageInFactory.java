package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.factory.AbstractRecursiveFactory;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
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
public class ProviderStageInFactory extends AbstractRecursiveFactory<OfferType, AbstractORQCalculator<?,?>> {

    @NotNull
    public ProviderStageInORQCalculator getInstance(@NotNull final OfferType key)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final ProviderStageInORQCalculator orqCalculator = new ProviderStageInORQCalculator();
        orqCalculator.setFactory(this);
        orqCalculator.setKey(key);
        return orqCalculator;
    }


    public void setup() {
    }
}
