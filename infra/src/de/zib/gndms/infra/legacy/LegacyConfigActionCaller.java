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

import de.zib.gndms.infra.system.ConfigActionCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 18.07.11  18:49
 * @brief
 */
public class LegacyConfigActionCaller  {

    private ConfigActionCaller actionCaller;
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );


    public String callAction( String className, String args ) throws Exception {

        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        actionCaller.callAction(className, args, pwriter);
        pwriter.flush();
        return swriter.toString();
    }


    public String getHelp( ) {
        try {
            return callAction( "help", "" );
        } catch ( Exception e ) {
            // exception on help calls are impossible...
            logger.error( "Action help throw exception", e );
            return ""; // save the day
        }
    }


    @Inject
    public void setActionCaller( ConfigActionCaller actionCaller ) {
        this.actionCaller = actionCaller;
    }
}
