package de.zib.gndms.taskflows.esgfStaging.client.model;

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.taskflows.esgfStaging.client.ESGFStagingTaskFlowMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:58
 * @brief The order class for a dummy taskflow.
 *
 * The dummy taskflow echos a given message a given number of times ^^ with some delay.
 * It can also fail on demand;
 */
public class ESGFStagingOrder extends AbstractOrder {

    private static final long serialVersionUID = -1925988463350176270L;

    final private List< String > urls;
    final private List< String > checksums;



    public ESGFStagingOrder() {
        super( );
        super.setTaskFlowType( ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY );

        urls = new LinkedList<String>( );
        checksums = new LinkedList<String>( );
    }


    public void addLink( final String url, final String checksum ) {
        urls.add( url );
        checksums.add( checksum );
    }


    @Override
    public String getTaskFlowType() {
        return ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY;
    }


    @NotNull
    @Override
    public String getDescription() {
        String s = "ESGF Staging for ";
        for( String u: urls ) {
            s = s + "; " + u;
        }
        return s;
    }


    @Override
    public String toString() {
        // TODO: generate toString method with a nice output
        return "ESGFStagingOrder{" +
            "urls = " +
            "}";
    }
}
