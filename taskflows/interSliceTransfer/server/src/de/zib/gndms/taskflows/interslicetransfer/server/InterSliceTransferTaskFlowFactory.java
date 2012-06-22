package de.zib.gndms.taskflows.interslicetransfer.server;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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


import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.interslicetransfer.client.InterSliceTransferMeta;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.server.logic.InterSliceTransferQuoteCalculator;
import de.zib.gndms.taskflows.interslicetransfer.server.logic.InterSliceTransferTaskAction;
import org.springframework.http.converter.HttpMessageConverter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;


/**
 * Factory for the inter-slice transfer TaskFlow
 *
 * @author  Maik Jorra
 * @date 2012-02-07
 */
// todo maybe this should subclass FileTransferTF for the config
public class InterSliceTransferTaskFlowFactory
    extends DefaultTaskFlowFactory<InterSliceTransferOrder, InterSliceTransferQuoteCalculator> {

    GNDMSystem system;


    public InterSliceTransferTaskFlowFactory() {

        super( InterSliceTransferMeta.INTER_SLICE_TRANSFER_KEY, InterSliceTransferQuoteCalculator.class,
                InterSliceTransferOrder.class );
    }


    @Override
    public InterSliceTransferQuoteCalculator getQuoteCalculator() {
        final InterSliceTransferQuoteCalculator quoteCalculator = super.getQuoteCalculator();
        injectMembers( quoteCalculator );
        return quoteCalculator;
    }


    @Override
    protected TaskFlow<InterSliceTransferOrder> prepare( TaskFlow<InterSliceTransferOrder> taskFlowOrder ) {
        return taskFlowOrder;
    }


    @Override
    protected Map<String, String> getDefaultConfig() {

        final HashMap<String, String> config = new HashMap<String, String>( 1 );
        // todo find a way to handle the shared config
        return config;
    }


    @Override
    public TaskAction createAction() {
        InterSliceTransferTaskAction action = new InterSliceTransferTaskAction();
        getInjector().injectMembers( action );
        action.setMessageConverter( getInjector().getInstance( "converter", HttpMessageConverter.class ) );

        // TODO: move these methods from TaskAction to TaskFlowFactory
        action.prepareRestTemplate(system.getSetupSSLFactory().getKeyPassword());
        action.prepareSliceClient();
        action.prepareSubspaceClient();

        return action;
    }


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    public void setSystem( GNDMSystem system ) {
        this.system = system;
    }
}
