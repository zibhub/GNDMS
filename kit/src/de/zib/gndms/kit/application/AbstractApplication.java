package de.zib.gndms.kit.application;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

/**
 * Abstract base class for a application with args4j support.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 06.11.2008, Time: 12:39:03
 */
public abstract class AbstractApplication {

    /**
     * When this method is called by an implementing subclass,
     * the fields of the class will be set as denoted by {@code args} and
     * {@link #run()} will be called.
     *
     * @see org.kohsuke.args4j.CmdLineParser#parseArgument(String[])
     * @param args a list of fields and their corresponding values, which will be set before {@code run()} is called
     *
     * @throws Exception if an error occures, while parsing the input String list.
     */
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

    /**
     * This method will be called after the fields of the class have been set.
     * @throws Exception
     */
    public abstract void run( ) throws Exception;
}
