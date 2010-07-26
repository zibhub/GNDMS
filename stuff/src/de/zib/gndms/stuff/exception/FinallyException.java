package de.zib.gndms.stuff.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 16:05:44
 */
public class FinallyException extends RuntimeException {

    private static final long serialVersionUID = 4402574484646696576L;

    private Throwable finallyException;


    public FinallyException() {
    }


    public FinallyException( String message ) {
        super( message );
    }


    public FinallyException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public FinallyException ( String message, Throwable cause, Throwable fin ) {
        super( message, cause );
        setFinallyException( fin );
    }

    @Override
    public void printStackTrace( PrintStream s ) {
        super.printStackTrace( s );
        if( finallyException != null ) {
            s.print(  "Followed by " + finallyException.getMessage() );
            finallyException.printStackTrace( s );
        }
    }


    @Override
    public void printStackTrace( PrintWriter s ) {
        super.printStackTrace( s );
        if( finallyException != null ) {
            s.print(  "Followed by " + finallyException.getMessage() );
            finallyException.printStackTrace( s );
        }
    }


    public Throwable getFinallyException() {
        return finallyException;
    }


    public void setFinallyException( Throwable finallyException ) {
        this.finallyException = finallyException;
    }
}
