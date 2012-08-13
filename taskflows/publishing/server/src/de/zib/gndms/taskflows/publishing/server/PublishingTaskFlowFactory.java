package de.zib.gndms.taskflows.publishing.server;


import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.SettableGridConfig;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.stuff.threading.PeriodicalJob;
import de.zib.gndms.taskflows.publishing.client.PublishingTaskFlowMeta;
import de.zib.gndms.taskflows.publishing.client.model.PublishingOrder;
import de.zib.gndms.voldmodel.Adis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.*;

/**
 * @author bachmann@zib.de
 * @date 01.07.12  12:14
 */
public class PublishingTaskFlowFactory extends DefaultTaskFlowFactory< PublishingOrder, PublishingQuoteCalculator > {

    private Dao dao;
    private GridConfig gridConfig;
    private VoldRegistrar registrar;
    private Adis adis;


    private TaskStatistics stats = new TaskStatistics();


    public PublishingTaskFlowFactory() {
        super( PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY, PublishingQuoteCalculator.class, PublishingOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }


    public PublishingQuoteCalculator getQuoteCalculator() {
        return new PublishingQuoteCalculator();
    }


    public TaskFlowInfo getInfo() {
        return new TaskFlowInfo() {
            private TaskStatistics statistics = stats;
            public TaskStatistics getStatistics() {
                return statistics;
            }


            public String getDescription() {
                return null;  // not required here
            }


            @Override
            public List<String> requiredAuthorization() {

                return PublishingTaskFlowMeta.REQUIRED_AUTHORIZATION;
            }
        };
    }


    public TaskFlow< PublishingOrder > create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }


    @Override
    protected TaskFlow< PublishingOrder > prepare( TaskFlow< PublishingOrder > publishingOrderTaskFlow ) {
        return publishingOrderTaskFlow;
    }


    public void delete( String id ) {
        stats.setActive( stats.getActive() - 1 );
        super.delete( id );
    }


    @Override
    protected Map<String, String> getDefaultConfig() {
        return new HashMap<String, String>( );
    }


    @Override
    public TaskAction createAction() {
        PublishingTFAction action = new PublishingTFAction(  );
        getInjector().injectMembers(action);

        return action;
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
            return "PublishingVoldRegistrar";
        }

        @Override
        public Long getPeriod() {
            final MapConfig config = new MapConfig( getConfigMapData() );

            return config.getLongOption( "updateInterval", 60000 );
        }

        @Override
        public void call() throws MandatoryOptionMissingException {
            final MapConfig config = new MapConfig( getConfigMapData() );

            if( !config.hasOption( "oidPrefix" ) ) {
                throw new IllegalStateException( "Dataprovider not configured: no OID_PREFIX given." );
            }

            final String name;
            if( !config.hasOption( "name" ) )
                name = config.getOption( "oidPrefix" );
            else
                name = config.getOption( "name" );

            // register publishing site itselfes
            adis.setPublisher( name, gorfxEP );

            // also register OID prefix of harvested files
            final Set< String > oidPrefixe = buildSet( config.getOption( "oidPrefix" ) );
            adis.setOIDPrefixe( gorfxEP, oidPrefixe );
        }
    }

    public Map< String, String > getConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType taskFlowType = session.findTaskFlowType( PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY );
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
