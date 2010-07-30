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
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

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
     * @param cp is ignored
     * @return a GridFTPClient, which is connected to server with the given hostname and port value.
     * @throws ServerException
     * @throws IOException
     */
    public GridFTPClient createClient( String host, int port, CredentialProvider cp ) throws ServerException, IOException {
        GridFTPClient clnt = new GridFTPClient( host, port );
        cp.installCredentials( clnt );
        return clnt;
    }
}
