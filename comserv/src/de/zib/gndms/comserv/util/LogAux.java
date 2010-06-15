package de.zib.gndms.comserv.util;

import de.zib.gndms.typecon.util.AxisTypeFromToXML;
import org.apache.log4j.Logger;
import org.globus.wsrf.security.SecurityManager;

import java.io.IOException;
import java.io.StringWriter;

public class LogAux {

    public static String loggableXSDT( Object o ) {

        if ( o == null )
            return "NULL";

        try {
            StringWriter sw = new StringWriter();
            AxisTypeFromToXML.toXML( sw, o, false, true );
            return sw.toString();
        } catch ( IOException e ) { // can hardly occure
            return "Object to xml conversion error. " + e.getMessage();
        }
    }


    public static void logSecInfo( Logger logger, String s ) throws org.globus.wsrf.security.SecurityException {

        logger.debug( "Method " + s + " called by: " +
            SecurityManager.getManager().getCaller() );
        String[] l = SecurityManager.getManager().getLocalUsernames();
        if ( l == null )
            logger.debug( "No mappings found" );
        else
            logger.debug( "Mapped to" + ( l.length > 0 ? l[0] : "none" ) );
    }
}