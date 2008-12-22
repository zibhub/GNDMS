package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 12:49:54
 */
public class FileTransferORQFactory extends InjectingRecursiveKeyFactory<OfferType, AbstractORQCalculator<?,?>> {
    @Override
    public AbstractORQCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final FileTransferORQCalculator orqCalculator = new FileTransferORQCalculator();
	    injectMembers(orqCalculator);
	    return orqCalculator;
    }
}
