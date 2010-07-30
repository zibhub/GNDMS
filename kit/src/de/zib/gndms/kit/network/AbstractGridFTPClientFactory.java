package de.zib.gndms.kit.network;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
