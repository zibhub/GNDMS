package de.zib.gndms.taskflows.publishing.client;

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


import de.zib.gndms.common.model.gorfx.types.TaskFlowMeta;
import de.zib.gndms.common.rest.CertificatePurpose;

import java.util.Collections;
import java.util.List;

/**
 * @author bachmann@zib.de
 * @date 31.07.2012 14:26
 * @brief Publishing constants.
 *
 */
public class PublishingTaskFlowMeta implements TaskFlowMeta {

    public final static String TASK_FLOW_TYPE_KEY ="PublishingTaskFlow";
    public final static List<String> REQUIRED_AUTHORIZATION = Collections.singletonList( CertificatePurpose.C3GRID.toString() );
    
    public final static String META_FILE = "meta.xml";

    // TODO: fill XSLT source for meta file transformation
    public final static String XSLT = "";

    private String description;


    public PublishingTaskFlowMeta( String description ) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }


    @Override
    public List<String> requiredAuthorization() {
        return REQUIRED_AUTHORIZATION;
    }


    public void setDescription( String description ) {
        this.description = description;
    }
}
