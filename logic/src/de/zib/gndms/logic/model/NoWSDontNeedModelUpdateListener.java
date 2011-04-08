package de.zib.gndms.logic.model;
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

import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.GridResourceItf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author try ma ik jo rr a zib
 * @date 07.04.11  14:58
 * @brief Rest version of GNDMS doesn't need this listener.
 *
 * It was designed to inform ws resources about changes on domain level.
 */
public class NoWSDontNeedModelUpdateListener implements ModelUpdateListener<GridResource> {

    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    public void onModelChange( GridResource model ) {
        logger.debug( "called for model " + model.getId() + " of " + model.getClass().getName() );
    }
}
