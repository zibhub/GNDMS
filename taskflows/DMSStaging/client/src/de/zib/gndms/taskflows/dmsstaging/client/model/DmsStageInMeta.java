package de.zib.gndms.taskflows.dmsstaging.client.model;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.model.gorfx.types.TaskFlowMeta;
import de.zib.gndms.common.rest.CertificatePurpose;

import java.util.Collections;
import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.05.12  17:34
 * @brief
 */
public class DmsStageInMeta implements TaskFlowMeta{


    public final static List<String> REQUIRED_AUTHORIZATION = Collections
            .singletonList( CertificatePurpose.C3GRID.toString() );
    public final static String DESCRIPTION = "Selects staging host, and acts as proxy.";
    public static final String DMS_STAGE_IN_KEY = "DmsStageIn";


    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public List<String> requiredAuthorization() {
        return REQUIRED_AUTHORIZATION;
    }
}
