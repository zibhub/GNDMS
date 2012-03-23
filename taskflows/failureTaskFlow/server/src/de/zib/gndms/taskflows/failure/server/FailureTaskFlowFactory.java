package de.zib.gndms.taskflows.failure.server;


import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.failure.client.FailureTaskFlowMeta;
import de.zib.gndms.taskflows.failure.client.model.FailureOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bachmann@zib.de
 * @date 08.03.12  16:10
 */
public class FailureTaskFlowFactory extends DefaultTaskFlowFactory< FailureOrder, FailureQuoteCalculator > {

    private TaskStatistics stats = new TaskStatistics();


    public FailureTaskFlowFactory() {
        super( FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY, FailureQuoteCalculator.class, FailureOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }


    public FailureQuoteCalculator getQuoteCalculator() {
        return new FailureQuoteCalculator( );
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

                return FailureTaskFlowMeta.REQUIRED_AUTHORIZATION;
            }
        };
    }


    public TaskFlow< FailureOrder > create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }


    @Override
    protected TaskFlow< FailureOrder > prepare( TaskFlow< FailureOrder > esgfStagingOrderTaskFlow ) {
        return esgfStagingOrderTaskFlow;
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
        FailureTFAction action = new FailureTFAction(  );
        getInjector().injectMembers( action );

        return action;
    }
}
