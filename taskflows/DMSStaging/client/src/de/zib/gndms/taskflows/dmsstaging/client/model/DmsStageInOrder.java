package de.zib.gndms.taskflows.dmsstaging.client.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
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
 */



import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import org.jetbrains.annotations.NotNull;

import java.lang.String;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 11:17:06
 */
public class DmsStageInOrder extends ProviderStageInOrder {

    private static final long serialVersionUID = 4197416353040463983L;

    private String gridSite;
    private String workflowId;


    public DmsStageInOrder(){
        super();
        super.setTaskFlowType( DmsStageInMeta.DMS_STAGE_IN_KEY );
    }

    @Override
    public @NotNull String getDescription() {
        return DmsStageInMeta.DESCRIPTION;
    }


    public void setGridSite( String gridSiteParam ){
	    gridSite = gridSiteParam;
    }


    public boolean hasGridSite(){
        return gridSite != null && gridSite.length()!=0;
    }


    public String getGridSite() {
        return gridSite;
    }


    public String getWorkflowId() {
        return workflowId;
    }


    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }


    public boolean hasWorkflowId(){
        return workflowId != null && workflowId.length() != 0;
    }
}

