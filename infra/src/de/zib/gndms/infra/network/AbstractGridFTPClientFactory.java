package de.zib.gndms.infra.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;

import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:31:30
 */
public abstract class AbstractGridFTPClientFactory implements GridFTPClientFactory {

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
