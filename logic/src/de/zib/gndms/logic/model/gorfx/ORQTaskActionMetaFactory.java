package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 11:04:52
 */
public class ORQTaskActionMetaFactory extends OfferTypeMetaFactory<ORQTaskAction<?>> {
    
    @Override
    public String getFactoryClassName(final OfferType key) {
        return key.getTaskActionFactoryClassName();
    }
}
