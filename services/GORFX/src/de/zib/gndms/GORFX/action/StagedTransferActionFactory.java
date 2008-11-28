package de.zib.gndms.GORFX.action;

import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.kit.factory.AbstractRecursiveFactory;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:44:10
 */
public class StagedTransferActionFactory  extends AbstractRecursiveFactory<OfferType, ORQTaskAction<?>> {
    @Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return new StagedTransferTaskAction();
    }

}
