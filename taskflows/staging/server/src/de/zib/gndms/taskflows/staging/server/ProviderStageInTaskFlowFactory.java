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
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.stuff.threading.PeriodicalJob;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.AbstractProviderStageInQuoteCalculator;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInAction;
import de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInQuoteCalculator;
import de.zib.gndms.voldmodel.Adis;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.*;


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
    private Adis adis;
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

    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setDao(Dao dao) {
        this.dao = dao;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setAdis( final Adis adis ) {
        this.adis = adis;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
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
        config.put( "updateInterval", "60000" ); // default: every minute
        //config.put( "oidPrefixe", "" );

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
            newInstance.setQuoteCalculator( getQuoteCalculator() );
            return newInstance;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }


    @PostConstruct
    public void startVoldRegistration() throws Exception {
        MapConfig config = new MapConfig( getConfigMapData() );
        if( !config.hasOption( "oidPrefixe" ) ) {
            logger.error( "Dataprovider not configured: no OID_PREFIXE given." );
        }
        
        registrar = new VoldRegistrar( adis, gridConfig.getBaseUrl() );
        registrar.start();
    }
    

    @PreDestroy
    public void stopVoldRegistration() {
        registrar.finish();
    }

    
    private class VoldRegistrar extends PeriodicalJob {
        final private Adis adis;
        final private String gorfxEP;
        public VoldRegistrar( final Adis adis, final String gorfxEP ) {
            this.adis = adis;
            this.gorfxEP = gorfxEP;
        }


        @Override
        public Integer getPeriod() {
            final MapConfig config = new MapConfig( getConfigMapData() );

            return config.getIntOption( "updateInterval", 60000 );
        }


        @Override
        public void call() throws Exception {
            final MapConfig config = new MapConfig( getConfigMapData() );

            if( !config.hasOption( "oidPrefixe" ) ) {
                throw new IllegalStateException( "Dataprovider not configured: no OID_PREFIXE given." );
            }

            final Set< String > oidPrefixe = buildSet( config.getOption( "oidPrefixe" ) );
            adis.setOIDPrefixe( gorfxEP, oidPrefixe );
        }

    }


    public Map< String, String > getConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType taskFlowType = session.findTaskFlowType( ProviderStageInMeta.PROVIDER_STAGING_KEY );
            final Map< String, String > configMapData = taskFlowType.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }

    
    private static Set< String > buildSet( String s ) {
        return new HashSet< String >( Arrays.asList( s.split( "(\\s|,|;)+" ) ) );
    }
}
