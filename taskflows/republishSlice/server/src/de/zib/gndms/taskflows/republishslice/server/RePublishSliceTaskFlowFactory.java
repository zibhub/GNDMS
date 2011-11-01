package de.zib.gndms.taskflows.republishslice.server;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.taskflows.republishslice.server.logic.RePublishSliceQuoteCalculator;


/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:37:55
 */
public class RePublishSliceTaskFlowFactory
{
	  // extends InjectingRecursiveKeyFactory<OfferType, AbstractQuotealculator<?,?>> {

    public AbstractQuoteCalculator<?> newInstance(final String keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final RePublishSliceQuoteCalculator sliceQuotealculator = new RePublishSliceQuoteCalculator();
	    injectMembers(sliceQuotealculator);
	    return sliceQuotealculator;
    }


   private void injectMembers( RePublishSliceQuoteCalculator sliceQuotealculator ) {
    } 
}
