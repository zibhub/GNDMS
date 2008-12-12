package de.zib.gndms.GORFX.action;

import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:37:55
 */
public class InterSliceTransferORQFactory 
	  extends InjectingRecursiveKeyFactory<OfferType, AbstractORQCalculator<?,?>> {

    @Override
    public AbstractORQCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final InterSliceTransferORQCalculator orqCalculator = new InterSliceTransferORQCalculator();
	    injectMembers(orqCalculator);
	    return orqCalculator;
    }
}
