package de.zib.gndms.infra.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;

import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 13:25:47
 */
public interface GridFTPClientFactory {

    public GridFTPClient createClient( String host  ) throws ServerException, IOException;
    public GridFTPClient createClient( String host, int port ) throws ServerException, IOException;
    public GridFTPClient createClient( URI uri ) throws ServerException, IOException;
}
