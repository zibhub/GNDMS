package de.zib.gndms.taskflows.publishing.client.model;

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.taskflows.publishing.client.PublishingTaskFlowMeta;
import org.jetbrains.annotations.NotNull;

/**
 * @author bachmann@zib.de
 * @date 31.07.2012  14:27
 * @brief The order class for the publishing taskflow.
 */
public class PublishingOrder extends AbstractOrder {

    private static final long serialVersionUID = 7794202120502583893L;

    private String sliceId;
    private String metadataFile;

    public PublishingOrder() {
        super( );
        super.setTaskFlowType( PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY );
    }


    public String getSliceId() {
        return sliceId;
    }


    public void setSliceId( String sliceId ) {
        this.sliceId = sliceId;
    }


    public String getMetadataFile() {
        return metadataFile;
    }


    public void setMetadataFile( String metadataFile ) {
        this.metadataFile = metadataFile;
    }

    @Override
    public String getTaskFlowType() {
        return PublishingTaskFlowMeta.TASK_FLOW_TYPE_KEY;
    }


    @NotNull
    @Override
    public String getDescription() {
        return "Publishing order for slice " + sliceId;
    }
}
