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
import de.zib.gndms.logic.action.NoSuchActionException;
import de.zib.gndms.logic.model.config.AvailableActionsAction;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 19-july-2011
 *
 * @brief A provider for config actions which is able to handle G2 DMS config actions.
 */
public class LegacyConfigActionProvider implements ConfigActionProvider {

    private SettableConfigMeta legacyConfigMeta;
    private boolean legacyConfigMetaInitialized = false;
    private LegacyConfigActionCaller legacyActionCaller; // TODO: create and inject this fellow


    public LegacyConfigActionProvider()	{	}


    private void initConfigMeta() {
        String help = legacyActionCaller.getHelp();
        legacyConfigMeta.setHelp( help );
        legacyConfigMetaInitialized=true;
    }


    public ConfigMeta getLegacyConfigMeta() {
        return legacyConfigMeta;
    }


    @Autowired
    public void setLegacyConfigMeta( SettableConfigMeta legacyConfigMeta ) {
        this.legacyConfigMeta = legacyConfigMeta;
    }


    @Override
    public List<String> listAvailableActions() {
        try {
            String actions = legacyActionCaller.callAction( "list", "" );
            return Arrays.asList( actions.split( String.valueOf( AvailableActionsAction.SEPARATOR_CHAR ) ) );
        } catch ( Exception e ) {
            // intentionally list action can't cause exception
        }
        return new ArrayList<String>( 0 );
    }


    @Override
    public ConfigMeta getMeta( String configName ) throws NoSuchActionException {

        if ( configName.equals( "legacy" ) ) {
            if( !legacyConfigMetaInitialized )
                initConfigMeta();

            return legacyConfigMeta;
        }

        throw new NoSuchActionException( configName );
    }


    /**
     * Convenience method which calls the config action directly.
     *
     * @param actionClassName The name of the action to call.
     * @param args The arguments for the action.
     * @return The result of the action as string.
     * @throws Exception An arbitrary thrown the config action itself.
     */
    @Override
    public String callConfigAction( String actionClassName, String args ) throws Exception {
        return legacyActionCaller.callAction( actionClassName, args );
    }


    @Override
    public ConfigAction getAction( String actionName ) throws NoSuchActionException {
        throw new NoSuchActionException( actionName );
    }
}
