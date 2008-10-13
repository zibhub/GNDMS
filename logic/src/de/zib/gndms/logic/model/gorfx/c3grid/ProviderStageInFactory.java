package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.factory.AbstractRecursiveFactory;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInFactory extends AbstractRecursiveFactory<OfferType, AbstractORQCalculator<?,?>> {

    @Override
    public AbstractORQCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return new ProviderStageInORQCalculator();
    }


    @Override
    public void setup() {
    }
}
