package de.zib.gndms.taskflows.filetransfer.server;

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
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.filetransfer.server.logic.FileTransferQuoteCalculator;

import java.util.Map;


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


    protected FileTransferTaskFlowFactory( String taskFlowKey, Class<FileTransferQuoteCalculator> calculatorClass, Class<FileTransferOrder> orderClass ) {
        super( taskFlowKey, calculatorClass, orderClass );
    }


    @Override
    public FileTransferQuoteCalculator getQuoteCalculator() {
        final FileTransferQuoteCalculator quoteCalculator = super.getQuoteCalculator();
        injectMembers( quoteCalculator );
        return quoteCalculator;
    }


    private void injectMembers( FileTransferQuoteCalculator quoteCalculator ) {
        // Implement Me. Pretty Please!!!
    }


    @Override
    protected TaskFlow<FileTransferOrder> prepare( TaskFlow<FileTransferOrder> fileTransferOrderTaskFlow ) {
        return fileTransferOrderTaskFlow;
    }


    @Override
    protected Map<String, String> getDefaultConfig() {
        return null;  // not required here
    }


    @Override
    public TaskAction createAction() {
        return null;  // not required here
    }
}
