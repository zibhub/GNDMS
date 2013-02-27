package de.zib.gndms.taskflows.voldregistration;

/*
 * Copyright 2008-2013 Zuse Institute Berlin (ZIB)
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
 * @author Ulrike Golas
 *
 */
public class VoldRegistrationTaskFlowMeta implements TaskFlowMeta {

    public final static String TASK_FLOW_TYPE_KEY ="VoldRegistrationTaskFlow";
    public final static List<String> REQUIRED_AUTHORIZATION
            = Collections.singletonList( CertificatePurpose.NONE.toString() );

    private String description;


    public VoldRegistrationTaskFlowMeta( String description ) {
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
