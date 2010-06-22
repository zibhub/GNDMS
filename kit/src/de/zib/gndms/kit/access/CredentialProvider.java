package de.zib.gndms.kit.access;

import java.util.List;

/**
 * Interface that hides the gredential mechanism.
 *
 * Implementations of this interface should provided the ability to install credentials in a specific object.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 10:12:35
 */
public interface CredentialProvider {

    /**
     * installs the credentials in the given object o.
     * @param o The object which accepts credentials, e.g. an instance of some client.
     */
    public void installCredentials( Object o );

    /**
     * Delivers the list of existing credentials.
     * @return A list of credentials which might be empty
     */
    public List getCredentials( );
}
