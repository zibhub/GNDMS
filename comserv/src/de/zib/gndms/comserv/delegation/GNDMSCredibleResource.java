package de.zib.gndms.comserv.delegation;

import org.globus.gsi.GlobusCredential;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.message.addressing.EndpointReferenceType;

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
