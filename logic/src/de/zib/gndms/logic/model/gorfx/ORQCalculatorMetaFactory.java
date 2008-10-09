package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 17:35:27
 */
public class ORQCalculatorMetaFactory extends OfferTypeMetaFactory<AbstractORQCalculator<?,?>> {
    @Override
    public String getFactoryClassName(final OfferType key) {
        return key.getCalculatorFactoryClassName();
    }

}