/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.dmsstaging.server;

import de.zib.gndms.common.model.gorfx.types.Quote;
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
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInMeta;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.dmsstaging.server.logic.DmsStageInQuoteCalculator;
import de.zib.gndms.taskflows.dmsstaging.server.logic.DmsStageInTaskAction;
import de.zib.gndms.voldmodel.Adis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @date: 19.06.12
 * @time: 10:48
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInTaskFlowFactory
        extends DefaultTaskFlowFactory< DmsStageInOrder, DmsStageInQuoteCalculator >
{

    private VoldRegistrar registrar;
    private Adis adis;
    private GridConfig gridConfig;

    private Dao dao;

    public DmsStageInTaskFlowFactory( ) {
        super( DmsStageInMeta.DMS_STAGE_IN_KEY,
                DmsStageInQuoteCalculator.class,
                DmsStageInOrder.class );
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setGridConfig( final SettableGridConfig gridConfig ) {
        this.gridConfig = gridConfig;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setAdis( final Adis adis ) {
        this.adis = adis;
    }


    public Dao getDao() {
        return dao;
    }


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    public void setDao( final Dao dao ) {
        this.dao = dao;
    }


    @Override
    public TaskFlow< DmsStageInOrder > prepare( TaskFlow< DmsStageInOrder > dmsStageInOrderTaskFlow ) {
        return dmsStageInOrderTaskFlow;
    }


    @Override
    public Map< String, String > getDefaultConfig( ) {
        HashMap< String, String > config = new HashMap< String, String >( 0 );

        config.put( "pollingInterval", "1000" ); // default: every second
        config.put( "updateInterval", "60000" ); // default: every minute

        return config;
    }


    @Override
    public TaskAction createAction( Quote quote ) {
        DmsStageInTaskAction taskAction = new DmsStageInTaskAction();
        
        taskAction.setAcceptedQuote( quote );
        
        injectMembers( taskAction );
        return taskAction;
    }


    @PostConstruct
    public void startVoldRegistration() throws Exception {
        registrar = new VoldRegistrar( adis, gridConfig.getBaseUrl() );
        registrar.start();
    }


    @PreDestroy
    public void stopVoldRegistration() {
        registrar.stop();
    }


    private class VoldRegistrar extends PeriodicalJob {
        final private Adis adis;
        final private String gorfxEP;
        public VoldRegistrar( final Adis adis, final String gorfxEP ) {
            this.adis = adis;
            this.gorfxEP = gorfxEP;
        }


        @Override
        public String getName() {
            return "DmsStageInVoldRegistrar";
        }


        @Override
        public Long getPeriod() {
            final MapConfig config = new MapConfig( getConfigMapData() );

            return config.getLongOption( "updateInterval", 60000 );
        }


        @Override
        public void call() throws Exception {
            adis.setDMS(gorfxEP);
        }
    }


    public Map< String, String > getConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType taskFlowType = session.findTaskFlowType( DmsStageInMeta.DMS_STAGE_IN_KEY );
            final Map< String, String > configMapData = taskFlowType.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }
}
