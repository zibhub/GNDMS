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



import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowFactory;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowProvider;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInTaskFlowFactory
	  implements TaskFlowFactory<ProviderStageInOrder, AbstractProviderStageInAction> {

    private Dao dao;

    @Override
    @NotNull
    public AbstractProviderStageInQuoteCalculator newInstance(@NotNull final String offerType)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull MapConfig config = new MapConfig(getDao().getOfferTypeConfig(offerType));
	    final Class<? extends AbstractProviderStageInQuoteCalculator> orqCalculatorClass =
		      config.getClassOption(
				    AbstractProviderStageInQuoteCalculator.class, "estimationClass",
				    ExternalProviderStageInOrderCalculator.class);
	    final AbstractProviderStageInQuoteCalculator instance = orqCalculatorClass.newInstance();
	    injectMembers(instance);
	    return instance;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }


    public static class Aux {

        public static TaskFlow fromTask( final Dao dao, final TaskFlowProvider provider, final String type,
                                         final String id ) {

            final Session ses = dao.beginSession();
            try {
                final Task t = ses.findTaskForResource( id );
                if ( t == null )
                    return null;
                final TaskFlowFactory tff = provider.getFactoryForTaskFlow( type );
                final TaskFlow tf = tff.createOrphan();
                tf.setId( t.getResourceId() );
                tf.setOrder( ( DelegatingOrder<?> ) t.getPayload() );
                tf.addQuote( quoteFromContract( t.getContract() ) );
                tf.setTaskling( t.getTaskling() );
                tff.adopt( tf );
                ses.success();
                return tf;
            } finally {
                ses.finish();
            }
        }


        public static Quote quoteFromContract( PersistentContract contract ) {

            return contract.toTransientContract();
        }
    }
}
