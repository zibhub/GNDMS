package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 12:50:48
 */
public class FileTransferActionFactory
	  extends InjectingRecursiveKeyFactory<OfferType, ORQTaskAction<?>> {

    @Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final FileTransferTaskAction taskAction = new FileTransferTaskAction();
	    injectMembers(taskAction);
	    return taskAction;
    }
}
