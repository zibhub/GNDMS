package de.zib.gndms.kit.access;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 14:14:42
 */
public interface RequiresCredentialProvider {

    void setCredentialProvider( CredentialProvider cp );

    CredentialProvider getCredentialProvider();
}
