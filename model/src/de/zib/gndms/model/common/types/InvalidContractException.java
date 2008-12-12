package de.zib.gndms.model.common.types;

import de.zib.gndms.model.common.PersistentContract;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 05.12.2008, Time: 14:43:34
 */
public class InvalidContractException extends Exception {

    public InvalidContractException( ) {
        super( );
    }

    
    public InvalidContractException( String msg ) {
        super( msg );
    }


    public InvalidContractException( PersistentContract con ) {
        super( con.toString( ) );
    }


    public InvalidContractException(  PersistentContract con, String msg ) {
        super( con.toString( ) + " " + msg );
    }
}
