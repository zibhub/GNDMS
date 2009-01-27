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


    public GNDMSystem getSystem() {
        return system;
    }


    public void setSystem( final GNDMSystem system ) {
        this.system = system;
    }
}
