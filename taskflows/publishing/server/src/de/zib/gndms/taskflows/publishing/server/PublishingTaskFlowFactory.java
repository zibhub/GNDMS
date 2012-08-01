package de.zib.gndms.taskflows.publishing.server;


import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.publishing.client.PublishingTaskFlowMeta;
import de.zib.gndms.taskflows.publishing.client.model.PublishingOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bachmann@zib.de
 * @date 01.07.12  12:14
 */
public class PublishingTaskFlowFactory extends DefaultTaskFlowFactory< PublishingOrder, PublishingQuoteCalculator > {

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
        getInjector().injectMembers( action );

        return action;
    }
}
