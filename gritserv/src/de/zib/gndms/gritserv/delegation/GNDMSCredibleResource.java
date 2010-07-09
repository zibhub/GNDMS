package de.zib.gndms.gritserv.delegation;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.gsi.GlobusCredential;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 10.02.2009, Time: 14:43:32
 */
public interface GNDMSCredibleResource {

    void setCredential ( GlobusCredential cred );
    GlobusCredential getCredential ( );

    void setDelegateEPR( EndpointReferenceType epr );
}
