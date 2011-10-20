package de.zib.gndms.infra.system;

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



import de.zib.gndms.infra.action.WSActionCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;


/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 27.01.2009, Time: 10:24:56
 *
 * Despite its name this class may still be useful in an non WS environment
 */
public class WSMaintenance {

    private GNDMSystem system;
    private final static Logger log = LoggerFactory.getLogger( WSMaintenance.class );


    public WSMaintenance( ) {
    }


    public WSMaintenance( final GNDMSystem system ) {
        this.system = system;
    }

    /**
     * Creates a ConfigAction and starts the ConfigAction.
     * Any output during the execution of the action will be returned
     *
     * Creation of the object is done according to the given String.
     *
     * The String is seperated into two parts.
     * The first part must be the classname of the desired ConfigAction.
     * The second part contain the parameters for the object.
     * See {@link de.zib.gndms.kit.config.ParameterTools#parseParameters(java.util.Map, String, java.util.regex.Pattern)}
     * about the required syntax.
     *
     * The strings must be seperated by {@code ' '}. 
     *
     * @param action a String containg the classname of a desired ConfigAction object and the parameters for the object
     * @return  Any output during the execution of the action
     * @throws Exception
     */
    public Object callMaintenenceAction( String action ) throws Exception {

        if( action == null || action.trim().length() == 0 )
            throw new IllegalStateException( "No action provided" );

        // remove classname from string:
        final int idx = action.indexOf( ' ' );
        String cn, parms;
        if( idx > 0 ) {
            cn = action.substring( 0, idx ).trim();
            parms = action.substring( idx ).trim();
        } else  {
            cn = action;
            parms = "";
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream( );
        PrintWriter printer = new PrintWriter( os );
        WSActionCaller ac = (WSActionCaller) system.getActionCaller();
        ac.callPublicAction( cn, parms, printer );
        printer.close();
        return os.toString();
    }

    /**
     * Returns the GNDMSystem
     * 
     * @return the GNDMSystem
     */
    public GNDMSystem getSystem() {
        return system;
    }


    public void setSystem( final GNDMSystem system ) {
        this.system = system;
    }
}
