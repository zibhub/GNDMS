package de.zib.gndms.kit.network;

import de.zib.gndms.kit.access.CredentialProvider;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;

/**
 * Default implementation of the GridFTPClientFactory interface.
 *
 * If {@code createClient(String host)} is invoked, number {@code 2811} will be used as default port value.
 * 
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:31:30
 */
public abstract class AbstractGridFTPClientFactory implements GridFTPClientFactory {

/**
     * Returns a GridFTPClient, which is connected to a server with the given hostname using '2811' as port value.
     *
     * @param host the hostname of the server
     * @param cp
 * @return a GridFTPClient, which is connected to the given hostname and a default port value will be used.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, CredentialProvider cp ) throws ServerException, IOException {
        return createClient( host, 2811, cp );
    }


    public GridFTPClient createClient( URI uri, CredentialProvider cp ) throws ServerException, IOException {
        String host = uri.getHost();
        if( host == null )
            throw new IllegalStateException( "host name required" );
        int port = uri.getPort( );
        if( port != -1 )
            return createClient( host, port, cp );
        else
            return createClient( host, cp );
    }


}
