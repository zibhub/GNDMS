package de.zib.gndms.comserv.util;

import de.zib.gndms.kit.access.CredentialProvider;
import org.globus.gsi.GlobusCredential;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 10:42:22
 */
public abstract class GlobusCredentialProvider implements CredentialProvider {

    private GlobusCredential credential;


    public GlobusCredential getCredential() {
        return credential;
    }


    public void setCredential( GlobusCredential credential ) {
        this.credential = credential;
    }


    public List getCredentials() {
        return new ArrayList<GlobusCredential>() {{ add( getCredential() ); }};
    }
}
