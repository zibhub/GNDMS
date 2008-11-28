package de.zib.gndms.model.gorfx.types.io;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.11.2008, Time: 15:17:35
 */
public class MandatoryPropertyMissingException extends IllegalStateException {
    private static final long serialVersionUID = 5871807791313619391L;


    public MandatoryPropertyMissingException( String s ) {
        super( s );
    }


    public MandatoryPropertyMissingException() {
    }
}
