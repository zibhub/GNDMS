package de.zib.gndms.taskflows.esgfStaging.client.model;
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

import de.zib.gndms.common.model.gorfx.types.TaskResult;

/**
 * @author try ma ik jo rr a zib
 * @date 14.03.11  11:12
 * @brief The result of the dummy task flow.
 *
 * Since the dummy taskflow only writes stuff, a string is enough here.
 */
public class ESGFStagingTaskFlowResult implements TaskResult<String> {

    private static final long serialVersionUID = 6090538263029074698L;
    private String result;


    public ESGFStagingTaskFlowResult() {
    }


    public ESGFStagingTaskFlowResult( String s ) {
        result = s;
    }


    public String getResult() {
        return result;
    }


    public void setResult( String result ) {
        this.result = result;
    }
}
