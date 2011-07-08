package de.zib.gndms.kit.network;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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
import java.net.URI;

/**
 * A Factory for a GridFTPClient.
 *
 * Either a {@link URI} must be denoted or a the hostname as String to connect the client to the server.
 * If no port number is denoted, a default value will be used,
 * when using {@code createClient(String host)}.
 *
 *
 * @see GridFTPClient
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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
