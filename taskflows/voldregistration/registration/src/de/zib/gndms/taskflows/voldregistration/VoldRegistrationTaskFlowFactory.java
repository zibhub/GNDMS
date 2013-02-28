package de.zib.gndms.taskflows.voldregistration;


import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.SettableGridConfig;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.voldmodel.Adis;
import de.zib.gndms.voldmodel.Type;
import de.zib.gndms.voldmodel.VolDRegistrar;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.*;

/**
 * @author golas@zib.de
 */
public class VoldRegistrationTaskFlowFactory extends DefaultTaskFlowFactory< VoldRegistrationOrder, VoldRegistrationQuoteCalculator > {

	private VolDRegistrar registrar;
	private Adis adis;
    private GridConfig gridConfig;

    private Dao dao;

    private TaskStatistics stats = new TaskStatistics();


    public VoldRegistrationTaskFlowFactory() {
        super( VoldRegistrationTaskFlowMeta.TASK_FLOW_TYPE_KEY, VoldRegistrationQuoteCalculator.class, VoldRegistrationOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }

    public @NotNull
    MapConfig getOfferTypeConfig() {
        return new MapConfig( getTaskFlowTypeConfigMapData() );
    }

    public Map<String, String> getTaskFlowTypeConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType ot = session.findTaskFlowType( VoldRegistrationTaskFlowMeta.TASK_FLOW_TYPE_KEY );
            final Map<String,String> configMapData = ot.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
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

                return VoldRegistrationTaskFlowMeta.REQUIRED_AUTHORIZATION;
            }
        };
    }


    public TaskFlow< VoldRegistrationOrder > create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }

    public void delete( String id ) {
        stats.setActive( stats.getActive() - 1 );
        super.delete( id );
    }


    @Override
    protected Map<String, String> getDefaultConfig() {
        final Map< String, String > defaultConfig = new HashMap<String, String>( );
        
        String voldUrl;
        try {
            voldUrl = gridConfig.getVoldUrl();
        } catch( Exception e ) {
            logger.debug( "This is not happening!", e );
            voldUrl = "";
        }

        defaultConfig.put( "updateInterval", "60000");
        defaultConfig.put( "baseUrl", voldUrl );
        
        return defaultConfig;
    }


    @Override
    public VoldRegistrationTFAction createAction( final Quote quote ) {
    	VoldRegistrationTFAction action = new VoldRegistrationTFAction(  );
        getInjector().injectMembers( action );

        try {
            action.setQuoteCalculator( getQuoteCalculator() );
        } catch (MandatoryOptionMissingException e) {
            throw new IllegalStateException( "Could not create Action, since it is not configured properly.", e );
        }
        action.setOwnDao( this.dao );

        return action;
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


    @PostConstruct
    public void startVoldRegistration() throws Exception {
        String gorfx = gridConfig.getBaseUrl();
        Type type = Type.CPID_GRAM;
        if( !getOfferTypeConfig().hasOption( "type" ) ) {
            logger.error( "type for Vold registation not set" );
        } else {
        	String ttype = getOfferTypeConfig().getOption("type");
        	try {
        		type = Type.valueOf(ttype); }
        	catch (IllegalArgumentException e) {}
        }
        if( !getOfferTypeConfig().hasOption( "sitName" ) ) {
            logger.error( "site name for Vold registation not set" );
        }
        String name = getOfferTypeConfig().getOption("siteName","");
        
        registrar = new VolDRegistrar( adis, gorfx, type, name, getOfferTypeConfig().getLongOption( "updateInterval" ));
        registrar.start();
    }


    @PreDestroy
    public void stopVoldRegistration() {
        registrar.stop();
    }

    public Map< String, String > getConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final TaskFlowType taskFlowType = session.findTaskFlowType( VoldRegistrationTaskFlowMeta.TASK_FLOW_TYPE_KEY );
            final Map< String, String > configMapData = taskFlowType.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }


	@Override
	protected TaskFlow<VoldRegistrationOrder> prepare(
			TaskFlow<VoldRegistrationOrder> taskFlow) {
		return taskFlow;
	}

}
