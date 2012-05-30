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

package de.zib.gndms.infra;

import org.jetbrains.annotations.NotNull;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 07.10.11  15:46
 * @brief
 */
public class SettableGridConfig extends GridConfig {

    private String gridName;
    private String gridPath;
    private String baseUrl;
    private String localBaseUrl;
    private String voldUrl;


    @NotNull
    @Override
    public String getGridName() throws Exception {
        return gridName;
    }


    @NotNull
    @Override
    public String getGridPath() throws Exception {
        return gridPath;
    }


    @NotNull
    @Override
    public String getBaseUrl() {
        return localBaseUrl;
    }


    @NotNull
    @Override
    public String getLocalBaseUrl() {
        return baseUrl;
    }


    @NotNull
    @Override
    public String getVoldUrl() throws Exception {
        return voldUrl;
    }


    public void setGridName( String gridName ) {
        this.gridName = gridName;
    }


    public void setGridPath( String gridPath ) {
        this.gridPath = gridPath;
    }


    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
    }


    public void setLocalBaseUrl( String localBaseUrl ) {
        this.localBaseUrl = localBaseUrl;
    }


    public void setVoldUrl( String voldUrl ) {
        this.voldUrl = voldUrl;
    }
}
