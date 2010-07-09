package de.zib.gndms.infra.grams;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.model.common.AccessMask;
import org.apache.log4j.Logger;
import org.globus.exec.generated.ScriptCommandEnumeration;
import org.globus.exec.service.exec.PerlJobDescription;

import java.io.IOException;
import java.util.HashMap;

/**
 * Implementation of the directory helpers for a linux system.
 *
 * This implementation uses the system call "chmod"
 */
class LinuxDirectoryAux implements DirectoryAux {

    protected Logger logger = Logger.getLogger( this.getClass() );

    final static String WX = "300";
    final static String RO = "500";
    final static String RW = "700";
    final static String EXECUTABLE = "executable";
    final static String ARGS = "arguments";

    public boolean setDirectoryReadWrite( String uid, String pth ) {
        return setMode( uid, RW, pth );
    }


    public boolean setDirectoryReadOnly( String uid, String pth ) {
        return setMode( uid, RO, pth );
    }


    public boolean setSubspacePermissions( String uid, String pth ) {
        return setMode( uid, WX, pth );
    }


    public boolean setPermissions( String uid, AccessMask perm, String pth ) {
        return setMode( uid, perm.toString(), pth );
    }


    private boolean setMode( String uid, String md, String pth ) {

        HashMap<String, Object> jd = new HashMap<String, Object>( 2 );
        jd.put( EXECUTABLE, "/bin/chmod" );
        jd.put( ARGS, new String[] { md, pth } );

        return executeGramsJob( uid, jd );
    }


    private boolean executeGramsJob( String uid, HashMap<String, Object> jd ) {

        PerlJobDescription pds = new PerlJobDescription( );
        // create from job description
        pds.putAll( jd );

        // add additional magic
        pds.put( "jobtype", "single" );
        pds.put(  "directory", "/tmp" );
        pds.put(  "stdin", "/dev/null" );
        pds.put(  "stdout", "/dev/stdout" );
        pds.put(  "stderr", "/dev/stderr" );

        String jds = pds.toPerlString();
        System.err.println( "perl job description: " + jds );

        GNDMSJobManagerScript jms = null;
        try {
            jms = new GNDMSJobManagerScript(
                uid,
                System.getenv( "GLOBUS_LOCATION" ),
                "fork", // globus job manager type
                ScriptCommandEnumeration.submit,
                jds,
                new String[]  {} // this is a possible context, lets see if it might be empty...
            );

            jms.run(); // run globus run
            int ec = jms.getError();
            if ( ec != 0 ) {
                logger.debug( "script exited with :" + ec );
                logger.debug( "Message: "+ jms.getFailureMessage() );
                logger.debug( "Destination: " + jms.getFailureDestination() );
                logger.debug( "Source: " + jms.getFailureSource() );
                return false;
            } else
               logger.debug( "Job successfull" );
        } catch ( IOException e ) {
            logger.error( e );
            return false;
        }

        return true;
    }


    public boolean changeOwner( String uid, String path) {
        throw new IllegalStateException( "method not implemented yet" );
        // return false;  // not required here
    }


    public boolean deleteDirectory(String owner, String pth) {
        throw new IllegalStateException( "method not implemented yet" );
        // return false;  // not required here
    }
}
