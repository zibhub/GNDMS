package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;

import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 17:08:39
 */
public class CertGridFTPClientFactory extends AbstractGridFTPClientFactory {

    public GridFTPClient createClient( String host, int port ) throws ServerException, IOException {

                                             // load CA certs (trusted) from resources classpath
        GridFTPClient cnt = new GridFTPClient( host , port );
        try {
            cnt.authenticate( getCredentials( ) );
        } catch ( Exception e ) {
            throw new IllegalStateException( e.getMessage(), e );
        }

        return cnt;
    }

    private static GlobusGSSCredentialImpl getCredentials() throws Exception {

		GlobusCredential gcred = new GlobusCredential("/tmp/x509up_u1000");
		System.out.println("GCRED: "+gcred.toString());
		return new GlobusGSSCredentialImpl(gcred, GSSCredential.DEFAULT_LIFETIME);
	}
}
