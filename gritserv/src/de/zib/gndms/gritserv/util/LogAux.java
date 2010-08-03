package de.zib.gndms.gritserv.util;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.gritserv.typecon.util.AxisTypeFromToXML;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.globus.wsrf.security.SecurityManager;

import java.io.IOException;
import java.io.PrintWriter;
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


    public static void logSecInfo( Logger logger, String methodName ) throws org.globus.wsrf.security.SecurityException {

        logger.info( "Method " + methodName + " called by: " +
            SecurityManager.getManager().getCaller() );

        String uid = getLocalName();
        if ( uid == null )
            logger.debug( "No mappings found" );
        else
            logger.info( "Mapped to " + uid );
    }


    public static String getLocalName( ) throws org.globus.wsrf.security.SecurityException {
        String[] l = SecurityManager.getManager().getLocalUsernames();
        if ( l != null && l.length > 0 )
            return l[0];

        return null;
    }


    public static Logger stdSetupLogger ( Class clazz ) {

        Logger logger = Logger.getLogger( clazz );
        ConsoleAppender app = new ConsoleAppender( );
        PatternLayout lay = new PatternLayout( );
        lay.setConversionPattern( "%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n" );
        app.setLayout( lay );
        app.setWriter( new PrintWriter( System.out ) );
        app.setTarget( "System.out");
        logger.addAppender( app );
        logger.setLevel( Level.DEBUG );
        return logger;
    }
}
