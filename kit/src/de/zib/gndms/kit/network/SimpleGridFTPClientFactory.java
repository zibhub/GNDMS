package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;

import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:29:57
 */
public class SimpleGridFTPClientFactory extends AbstractGridFTPClientFactory {

    public GridFTPClient createClient( String host, int port ) throws ServerException, IOException {
        GridFTPClient clnt = new GridFTPClient( host, port );
        clnt.authenticate( null );
        return clnt;
    }
}
