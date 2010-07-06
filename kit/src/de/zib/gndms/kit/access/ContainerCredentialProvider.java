package de.zib.gndms.kit.access;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 22.06.2010, Time: 10:13:24
 */
public class ContainerCredentialProvider implements CredentialProvider {

    private static GlobusCredential cred;
    static {
        try {
            cred = GlobusCredential.getDefaultCredential();
            System.err.println( "****** Container Cert: " ); 
            cred.save( System.err );
        } catch ( GlobusCredentialException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void installCredentials( Object o ) {
        try {
            GridFTPClient.class.cast( o ).authenticate( new GlobusGSSCredentialImpl( cred, GSSCredential.DEFAULT_LIFETIME) );
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( ServerException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( GSSException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public List getCredentials() {
        return new ArrayList( 1 )  {{ add( cred ); }};
    }
}
