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


import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.SettableGridConfig;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInQuoteCalculator;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInQuoteCalculator;
import de.zib.vold.client.VolDClient;
import de.zib.vold.common.Key;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
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

    private VoldRegistrar registrar;
    private VolDClient volDClient;
    private GridConfig gridConfig;

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
            final String clazz = config.getOption( "estimationClass",
                    ExternalProviderStageInQuoteCalculator.class.getName() );
            final Class<? extends AbstractProviderStageInQuoteCalculator> orqCalculatorClass =
                    Class.forName( clazz ).asSubclass( AbstractProviderStageInQuoteCalculator
                            .class);
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

    @Inject
    public void setDao(Dao dao) {
        this.dao = dao;
    }


    @Inject
    public void setVolDClient( final VolDClient volDClient ) {
        this.volDClient = volDClient;
    }


    @Inject
    public void setGridConfig( final SettableGridConfig gridConfig ) {
        this.gridConfig = gridConfig;
    }


    @Override
    protected TaskFlow<ProviderStageInOrder> prepare( TaskFlow<ProviderStageInOrder> providerStageInOrderTaskFlow ) {
        return providerStageInOrderTaskFlow;
    }


    /**
     * Delivers the default config.
     *
     * The default config uses external provider stagin, i.e. script based staging.
     * @return The default config as map.
     */
    @Override
    protected Map<String, String> getDefaultConfig() {

        HashMap<String, String> config = new HashMap<String, String>( 2 );
        config.put( "subspace", "providerStaging" );
        config.put( "sliceKind", "staging" );
        config.put( "stagingClass", "de.zib.gndms.logic.model.gorfx.c3grid" +
                                    ".ExternalProviderStageInAction" );
        config.put( "estimationClass", "de.zib.gndms.logic.model.gorfx.c3grid" +
                                       ".ExternalProviderStageInORQCalculator" );
        config.put( "oidPrefixe", "" );

        return config;
    }


    @Override
    public TaskAction createAction() {
        
        final @NotNull MapConfig config = new MapConfig(getDao().getTaskFlowTypeConfig( getTaskFlowKey() ));
        final Class<? extends AbstractProviderStageInAction> instanceClass;
        try {

            String clazz = config.getOption( "stagingClass",
                    ExternalProviderStageInAction.class.getName() );
            instanceClass =
                    Class.forName( clazz ).asSubclass( AbstractProviderStageInAction.class );
            final AbstractProviderStageInAction newInstance = instanceClass.newInstance();
      	    injectMembers(newInstance);
            return newInstance;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }


    @PostConstruct
    public void startVoldRegistration() throws Exception {
        registrar = new VoldRegistrar( volDClient, gridConfig.getBaseUrl() );
        registrar.start();
    }

    @PreDestroy
    public void stopVoldRegistration() {
        registrar.finish();
    }

    private class VoldRegistrar extends Thread {
        final private VolDClient vold;
        final private String gorfxEP;
        private boolean run = true;


        public VoldRegistrar( final VolDClient volDClient, final String gorfxEP ) {
            this.vold = volDClient;
            this.gorfxEP = gorfxEP;
        }
        
        
        public void finish() {
            run = false;
            try {
                this.join();
            } catch( InterruptedException e ) {
            }
        }


        @Override
        public void run() {
            while( run ) {
                try {
                    Thread.sleep( 5000 );
                } catch( InterruptedException e ) {
                    run = false;
                    return;
                }

                try {
                vold.insert("", new Key("/c3grid/", "dp", "gorfxep"), new HashSet<String>() {{
                    add( gorfxEP );
                }});
                }
                catch( Exception e ) {
                    logger.error( "Could not register dataprovider. I'll try again in 5 seconds...", e );
                }
            }
        }
    }
}
