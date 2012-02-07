package de.zib.gndms.logic.model;
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

import de.zib.gndms.common.model.gorfx.types.Order;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 20.12.11  12:11
 * @brief
 */
public class ModelIdHoldingOrder implements Order {

    private static final long serialVersionUID = 3318131130502306454L;
    private String modelId;


    public ModelIdHoldingOrder() {

    }


    public ModelIdHoldingOrder( final String modelId ) {

        this.modelId = modelId;
    }


    public String getModelId() {

        return modelId;
    }


    public void setModelId( final String modelId ) {

        this.modelId = modelId;
    }


    @Override
    public String getTaskFlowType() {

        return "ModelTaskAction";
    }


    @Override
    public boolean isJustEstimate() {

        return false;  // not required here
    }
}
