package de.zib.gndms.comserv.tests;

import de.zib.gndms.comserv.util.GNDMSJobManagerScript;
import de.zib.gndms.kit.application.AbstractApplication;
import org.globus.exec.generated.ScriptCommandEnumeration;
import org.globus.exec.service.exec.PerlJobDescription;
import org.kohsuke.args4j.Option;

import java.io.IOException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 01.07.2010, Time: 17:52:20
 */
public class GramsMockUp extends AbstractApplication {

    @Option( name = "-u", required = true )
    String uid;

    @Option( name = "-p", required = true, usage = "complete dir path to create" )
    String path;


    public void createDirectory( String uid, String path  ) throws IOException {

        PerlJobDescription pds = new PerlJobDescription( );
        pds.put( "executable", "/bin/mkdir" );
        //pds.put( "arguments", "-p " + path );
        pds.put( "arguments", new String[] {"-p", path } );
        pds.put( "jobtype", "single" );
        // working dir is hardcoded user.home i.e. /home/globus  AWESOME!!
        pds.put(  "directory", "/tmp" );
        pds.put(  "stdin", "/dev/null" );
        pds.put(  "stdout", "/dev/stdout" );
        pds.put(  "stderr", "/dev/stderr" );

        String jd = pds.toPerlString();
        System.err.println( "perl job description: " + jd );

        GNDMSJobManagerScript jms = new GNDMSJobManagerScript(
            uid,
            System.getenv( "GLOBUS_LOCATION" ),
            "fork", // globus job manager type
            ScriptCommandEnumeration.submit,
            jd,
            new String[]  {} // this is a possible context, lets see if it might be empty...
            );

        jms.run(); // run globus run
        System.out.println( jms.getFailureMessage() );
    }


    public static void main( String args[] ) throws Exception {
        (new GramsMockUp() ).run( args );
    }


    public void run() throws Exception {

        System.err.println( "GlobLoc: "+ System.getenv( "GLOBUS_LOCATION" ) );
        System.err.println( "env.GlobLoc: "+ System.getProperty( "ENV.GLOBUS_LOCATION" ) );

        createDirectory( uid, path );
    }
}
