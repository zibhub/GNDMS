package de.zib.gndms.logic.model.gorfx.c3grid;

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
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.NonReadableChannelException;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInTaskFlowFactory
	  extends DefaultTaskFlowFactory<ProviderStageInOrder, ExternalProviderStageInQuoteCalculator> {

    private Dao dao;
    private String taskFlowType; // todo initialise


    public ProviderStageInTaskFlowFactory( ) {
        // todo problem ExternalProvider.. is to concrete AbstractProvider.. would be better
        super( ProviderStageInMeta.PROVIDER_STAGING_KEY, ExternalProviderStageInQuoteCalculator.class,
            ProviderStageInOrder.class );
    }

    @Override
    public ExternalProviderStageInQuoteCalculator getQuoteCalculator() {

        ExternalProviderStageInQuoteCalculator calculon = super.getQuoteCalculator();    // overriden method implementation

        final @NotNull MapConfig config = new MapConfig(getDao().getTaskFlowTypeConfig( taskFlowType ));
        try {
            final Class<? extends AbstractProviderStageInQuoteCalculator> orqCalculatorClass =
                config.getClassOption(
                    AbstractProviderStageInQuoteCalculator.class, "estimationClass",
                    ExternalProviderStageInQuoteCalculator.class );
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeException( e );
        }
        // injectMembers(instance);
        return calculon;
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
    public TaskAction createAction() {
        /*
        final @NotNull MapConfig config = new MapConfig(getDao().getTaskFlowTypeConfig( offerType ));
	    final Class<? extends AbstractProviderStageInAction> instanceClass = config.getClassOption(
		      AbstractProviderStageInAction.class, "stagingClass",
		      ExternalProviderStageInAction.class);
	    final AbstractProviderStageInAction newInstance = instanceClass.newInstance();
	    injectMembers(newInstance);
	    */
        return null;  // todo check which action to create
    }
}
