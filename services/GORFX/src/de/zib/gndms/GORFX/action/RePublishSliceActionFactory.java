package de.zib.gndms.GORFX.action;

import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.types.factory.AbstractRecursiveKeyFactory;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:44:10
 */
public class RePublishSliceActionFactory  extends AbstractRecursiveKeyFactory<OfferType, ORQTaskAction<?>> {
    @Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return new RePublishSliceTaskAction();
    }

}
