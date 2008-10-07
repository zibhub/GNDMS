package de.zib.gndms.kit.factory;

import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 17:56:30
 */
public abstract class AbstractFactory<T extends FactoryInstance<T>> implements Factory<T> {
    private final OfferType offerType;

    public AbstractFactory(final OfferType ot) {
        offerType = ot;
    }


    protected OfferType getOfferType() {
        return offerType;
    }
}
