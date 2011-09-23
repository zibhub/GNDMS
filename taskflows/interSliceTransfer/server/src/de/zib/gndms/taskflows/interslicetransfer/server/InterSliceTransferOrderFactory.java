/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.taskflows.interslicetransfer.server;

import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;


/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:37:55
 */
public class InterSliceTransferOrderFactory
	  extends InjectingRecursiveKeyFactory<OfferType, AbstractOrderCalculator<?,?>> {

    @Override
    public AbstractOrderCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final de.zib.gndms.GORFX.action.InterSliceTransferOrderCalculator orqCalculator = new InterSliceTransferORQCalculator();
	    injectMembers(orqCalculator);
	    return orqCalculator;
    }
}
