package de.zib.gndms.kit.network;

import de.zib.gndms.kit.access.CredentialProvider;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;

import java.io.IOException;

/**
 * A GridFTPClient factory for clients with a required GSSCredential authentification.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 17:08:39
 */
public class CertGridFTPClientFactory extends AbstractGridFTPClientFactory {

    /**
     * Returns a GridFTPClient, which is connected to a server with the given hostname using the denoted port value.
     * A GSSCredential authentification is required. Credential must be found at {@code /tmp/x509up_u1000}
     *
     * THIS IS JUST FOR TESTING.
     *
     * @param host the hostname of the server
     * @param port the port value used for a connection to the server
     * @param cp is ignored
     * @return a GridFTPClient, which is connected to server with the given hostname and port value.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, int port, CredentialProvider cp ) throws ServerException, IOException {

        // load CA certs (trusted) from resources classpath
        GridFTPClient cnt = new GridFTPClient( host , port );
        try {
            cnt.authenticate( getCredentials( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( e.getMessage(), e );
        }

        return cnt;
    }

    /**
     * Returns the GlobusCredential based on {@code /tmp/x509up_u1000}.
     *
     * @return the GlobusCredential based on {@code /tmp/x509up_u1000}.
     * @throws Exception
     */
    private static GlobusGSSCredentialImpl getCredentials() throws Exception {

        GlobusCredential gcred = new GlobusCredential("/tmp/x509up_u1000");
        System.out.println("GCRED: "+gcred.toString());
        return new GlobusGSSCredentialImpl(gcred, GSSCredential.DEFAULT_LIFETIME);
    }
}
