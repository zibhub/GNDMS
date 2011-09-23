/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.infra.legacy;

import de.zib.gndms.common.logic.config.ConfigMeta;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 18.07.11  15:52
 * @brief
 */
public class SettableConfigMeta implements ConfigMeta {

    private String name;
    private String help;
    private String description;

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getHelp() {
        return help;
    }


    @Override
    public String getDescription() {
        return description;
    }


    public void setName( String name ) {
        this.name = name;
    }


    public void setHelp( String help ) {
        this.help = help;
    }


    public void setDescription( String description ) {
        this.description = description;
    }
}
