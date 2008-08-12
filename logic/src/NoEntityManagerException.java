/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.08.2008, Time: 16:06:17
 */
public class NoEntityManagerException extends Exception {

    public NoEntityManagerException( ) {
        super( "No entity menager provided");
    }
}
