package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;

import java.io.IOException;

/**
 * A GridFTPClient factory for clients without a GSSCredential authentification.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:29:57
 */
public class SimpleGridFTPClientFactory extends AbstractGridFTPClientFactory {

    /**
     * Returns a GridFTPClient, which is connected to a server with the given hostname using the denoted port value.
     * No GSSCredential authentification is required.
     *
     * @param host the hostname of the server
     * @param port the port value used for a connection to the server
     * @return a GridFTPClient, which is connected to server with the given hostname and port value.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, int port ) throws ServerException, IOException {
        GridFTPClient clnt = new GridFTPClient( host, port );
        clnt.authenticate( null );
        return clnt;
    }
}
