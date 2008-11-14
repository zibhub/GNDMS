package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.kit.factory.AbstractRecursiveFactory;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:37:55
 */
public class SliceStageInORQFactory extends AbstractRecursiveFactory<OfferType, AbstractORQCalculator<?,?>> {

    @Override
    public AbstractORQCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return new SliceStageInORQCalculator();
    }
}
