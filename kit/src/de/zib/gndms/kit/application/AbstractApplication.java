package de.zib.gndms.kit.application;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

/**
 * Abstract base class for a application with args4j support.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 06.11.2008, Time: 12:39:03
 */
public abstract class AbstractApplication {

    public void run( String[] args ) throws Exception {

        CmdLineParser pars = new CmdLineParser( this );
        try{
            pars.parseArgument( args );
            this.run();
        } catch ( CmdLineException e ) {
            System.out.println( e.getMessage( ) );
            pars.printUsage( System.out );
        }
    }


    public abstract void run( ) throws Exception;
}
