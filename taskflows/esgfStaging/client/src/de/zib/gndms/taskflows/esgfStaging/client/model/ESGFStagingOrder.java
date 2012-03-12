package de.zib.gndms.taskflows.esgfStaging.client.model;

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.taskflows.esgfStaging.client.ESGFStagingTaskFlowMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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

    final private Map< String, String > urls;


    public ESGFStagingOrder() {
        super( );
        super.setTaskFlowType( ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY );

        urls = new HashMap< String, String >( );
    }


    /**
     * Add an url of a file and its checksum to the staging order.
     *
     * @param url The url of the file to stage.
     * @param checksum The checksum of the file to stage.
     */
    public void addLink( final String url, final String checksum ) {
        urls.put( url, checksum );
    }
    
    
    public Map< String, String > getUrls( ) {
        return urls;
    }


    @Override
    public String getTaskFlowType() {
        return ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY;
    }


    @NotNull
    @Override
    public String getDescription() {
        String s = "ESGF Staging for ";
        for( String u: urls.keySet() ) {
            s = s + "; " + u;
        }
        return s;
    }
}
