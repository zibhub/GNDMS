package de.zib.gndms.kit.network;

import de.zib.gndms.kit.access.CredentialProvider;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;

/**
 * A Factory for a GridFTPClient.
 *
 * Either a {@link URI} must be denoted or a the hostname as String to connect the client to the server.
 * If no port number is denoted, a default value will be used,
 * when using {@code createClient(String host)}.
 *
 *
 * @see GridFTPClient
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:25:47
 */
public interface GridFTPClientFactory {

    /**
     * Returns a GridFTPClient, which is connected to a server with the given hostname using a default port value.
     * 
     * @param host the hostname of the server
     * @param cp a provider for required credential can be null.
     * @return a GridFTPClient, which is connected to the given hostname and a default port value will be used.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, CredentialProvider cp ) throws ServerException, IOException;

    /**
     * Returns a GridFTPClient, which is connected to a server with the given hostname using the denoted port value.
     *
     * @param host the hostname of the server
     * @param port the port value used for a connection to the server
     * @param cp a provider for required credential can be null.
     * @return a GridFTPClient, which is connected to server with the given hostname and port value.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, int port, CredentialProvider cp ) throws ServerException, IOException;

   /**
     * Returns a GridFTPClient, which is connected to a server corresponding to the given URI.
     *
     * @param uri a uri describing the connection to the server
    * @param cp a provider for required credential can be null.
    * @return a GridFTPClient, which is connected to a server corresponding to the given URI.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( URI uri, CredentialProvider cp ) throws ServerException, IOException;
}
