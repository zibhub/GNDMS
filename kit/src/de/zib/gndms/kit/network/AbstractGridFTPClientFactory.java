package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;

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
     * @return a GridFTPClient, which is connected to the given hostname and a default port value will be used.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host ) throws ServerException, IOException {
        return createClient( host, 2811 );
    }


    public GridFTPClient createClient( URI uri ) throws ServerException, IOException {
        String host = uri.getHost();
        if( host == null )
            throw new IllegalStateException( "host name required" );
        int port = uri.getPort( );
        if( port != -1 )
            return createClient( host, port );
        else
            return createClient( host );
    }
}
