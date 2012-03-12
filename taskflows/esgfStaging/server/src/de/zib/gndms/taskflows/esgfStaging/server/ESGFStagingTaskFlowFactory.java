package de.zib.gndms.taskflows.esgfStaging.server;


import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.esgfStaging.client.ESGFStagingTaskFlowMeta;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bachmann@zib.de
 * @date 08.03.12  16:10
 */
public class ESGFStagingTaskFlowFactory extends DefaultTaskFlowFactory< ESGFStagingOrder, ESGFStagingQuoteCalculator > {

    private TaskStatistics stats = new TaskStatistics();


    public ESGFStagingTaskFlowFactory() {
        super( ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY, ESGFStagingQuoteCalculator.class, ESGFStagingOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }


    public ESGFStagingQuoteCalculator getQuoteCalculator() {
        return new ESGFStagingQuoteCalculator( );
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

                return ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION;
            }
        };
    }


    public TaskFlow< ESGFStagingOrder > create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }


    @Override
    protected TaskFlow< ESGFStagingOrder > prepare( TaskFlow< ESGFStagingOrder > esgfStagingOrderTaskFlow ) {
        return esgfStagingOrderTaskFlow;
    }


    public void delete( String id ) {
        stats.setActive( stats.getActive() - 1 );
        super.delete( id );
    }


    @Override
    protected Map<String, String> getDefaultConfig() {
        Map< String, String > defaultConfig = new HashMap<String, String>( );
        
        defaultConfig.put( "subspace", "providerStaging" );
        defaultConfig.put( "sliceKind", "staging");
        
        return defaultConfig;
    }


    @Override
    public TaskAction createAction() {
        ESGFStagingTFAction action = new ESGFStagingTFAction(  );
        getInjector().injectMembers( action );

        return action;
    }

}
