package de.zib.gndms.model;


/**
 * Shared super class of all model objects.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 17:03:24
 *
 * NOTE: do not declare this class abstract!! ( gives compiler trouble )
 */

public class ModelObject {

    protected ModelObject( ) {

    }
    

    public static int hashCode0(Object obj) {
		return obj == null ? 0 : obj.hashCode();
	}

}

