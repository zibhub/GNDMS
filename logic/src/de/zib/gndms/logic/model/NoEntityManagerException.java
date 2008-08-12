package de.zib.gndms.logic.model;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.08.2008, Time: 16:06:17
 */
public class NoEntityManagerException extends RuntimeException {
	private static final long serialVersionUID = -5819345104409948501L;


	public NoEntityManagerException( ) {
        super( "No entity menager provided");
    }
}
