package de.zib.gndms.taskflows.staging.server;

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



import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInQuoteCalculator;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInQuoteCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInTaskFlowFactory
	  extends DefaultTaskFlowFactory<ProviderStageInOrder,
        AbstractProviderStageInQuoteCalculator> {

    private Dao dao;

    public ProviderStageInTaskFlowFactory( ) {
        super( ProviderStageInMeta.PROVIDER_STAGING_KEY,
                AbstractProviderStageInQuoteCalculator.class,
                ProviderStageInOrder.class );
    }

    @Override
    public AbstractProviderStageInQuoteCalculator getQuoteCalculator() {

        AbstractProviderStageInQuoteCalculator calculon;

        final @NotNull MapConfig config = new MapConfig( getDao().getTaskFlowTypeConfig(
                getTaskFlowKey() ));
        try {
            final Class<? extends AbstractProviderStageInQuoteCalculator> orqCalculatorClass =
                config.getClassOption(
                    AbstractProviderStageInQuoteCalculator.class, "estimationClass",
                    ExternalProviderStageInQuoteCalculator.class );
            calculon = orqCalculatorClass.newInstance();
            injectMembers( calculon );
            return calculon;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }


    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }


    @Override
    protected TaskFlow<ProviderStageInOrder> prepare( TaskFlow<ProviderStageInOrder> providerStageInOrderTaskFlow ) {
        return providerStageInOrderTaskFlow;
    }


    @Override
    protected Map<String, String> getDefaultConfig() {
        return null;  // not required here
    }


    @Override
    public TaskAction createAction() {
        
        final @NotNull MapConfig config = new MapConfig(getDao().getTaskFlowTypeConfig( getTaskFlowKey() ));
        final Class<? extends AbstractProviderStageInAction> instanceClass;
        try {
            instanceClass = config.getClassOption(
                  AbstractProviderStageInAction.class, "stagingClass",
                  ExternalProviderStageInAction.class);
            final AbstractProviderStageInAction newInstance = instanceClass.newInstance();
      	    injectMembers(newInstance);
            return newInstance;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}
