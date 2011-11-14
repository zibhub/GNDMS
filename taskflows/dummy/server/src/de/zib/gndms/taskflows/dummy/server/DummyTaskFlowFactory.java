package de.zib.gndms.taskflows.dummy.server;


import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.model.gorfx.types.TaskStatistics;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.model.gorfx.taskflow.DefaultTaskFlowFactory;
import de.zib.gndms.neomodel.gorfx.TaskFlow;
import de.zib.gndms.taskflows.dummy.client.DummyTaskFlowMeta;
import de.zib.gndms.taskflows.dummy.client.model.DummyOrder;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:10
 * @brief Provider of dummy taskflows things.
 */
public class DummyTaskFlowFactory extends DefaultTaskFlowFactory<DummyOrder,DummyQuoteCalculator> {

    private TaskStatistics stats = new TaskStatistics();


    public DummyTaskFlowFactory() {

        super( DummyTaskFlowMeta.TASK_FLOW_KEY, DummyQuoteCalculator.class, DummyOrder.class );
    }


    @Override
    public int getVersion() {
        return 0;  // not required here
    }


    public DummyQuoteCalculator getQuoteCalculator() {
        return new DummyQuoteCalculator( );
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
        };
    }


    public TaskFlow<DummyOrder> create() {
        stats.setActive( stats.getActive() + 1 );
        return super.create();
    }


    @Override
    protected TaskFlow<DummyOrder> prepare( TaskFlow<DummyOrder> dummyOrderTaskFlow ) {
        return dummyOrderTaskFlow;
    }


    public void delete( String id ) {

        stats.setActive( stats.getActive() - 1 );
        super.delete( id );
    }



    @Override
    public TaskAction createAction() {
        return new DummyTFAction(  );
    }

}
