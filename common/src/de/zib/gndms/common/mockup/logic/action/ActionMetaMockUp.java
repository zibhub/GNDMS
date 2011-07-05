package de.zib.gndms.common.mockup.logic.action;
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

import de.zib.gndms.common.logic.action.ActionMeta;

/**
 * @author try ma ik jo rr a zib
 * @date 16.02.11  12:45
 * @brief Mock-up implementation.
 * @deprecated
 */
public abstract class ActionMetaMockUp implements ActionMeta {

    private String configName;
    private String configHelp;
    private String configDescription;


    public String getName() {
        return configName;
    }


    public String getHelp() {
        return configHelp;
    }


    public String getDescription() {
        return configDescription;
    }


    public void setConfigDescription( String configDescription ) {
        this.configDescription = configDescription;
    }


    public void setConfigHelp( String configHelp ) {
        this.configHelp = configHelp;
    }


    public void setConfigName( String configName ) {
        this.configName = configName;
    }
}
