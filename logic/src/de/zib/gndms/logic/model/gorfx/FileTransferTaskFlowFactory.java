package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.types.FileTransferOrder;
import de.zib.gndms.neomodel.gorfx.TaskFlow;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 12:49:54
 */
public class FileTransferTaskFlowFactory
    extends DefaultTaskFlowFactory<FileTransferOrder, FileTransferQuoteCalculator> {

    @Override
    public AbstractQuoteCalculator<?> newInstance(final String offerType)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final FileTransferQuoteCalculator quoteCalculator = new FileTransferQuoteCalculator();
	    injectMembers( quoteCalculator );
	    return quoteCalculator;
    }


    @Override
    protected TaskFlow<FileTransferOrder> prepare( TaskFlow<FileTransferOrder> fileTransferOrderTaskFlow ) {
        return null;  // not required here
    }


    @Override
    public TaskAction createAction() {
        return null;  // not required here
    }
}
