package de.zib.gndms.infra.system;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import de.zib.gndms.infra.action.WSActionCaller;


/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 27.01.2009, Time: 10:24:56
 */
public class WSMaintenance {

    private GNDMSystem system;
    private final static Logger log = Logger.getLogger( WSMaintenance.class );


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
        final String cn = action.substring( 0, idx ).trim();
        final String parms = action.substring( idx ).trim();

        ByteArrayOutputStream os = new ByteArrayOutputStream( );
        PrintWriter printer = new PrintWriter( os );
        WSActionCaller ac = (WSActionCaller) system.getActionCaller();
        ac.callPublicAction( cn, parms, printer );
        printer.close();
        String res = os.toString();
        log.debug( "returning " + res );
        return res;
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
