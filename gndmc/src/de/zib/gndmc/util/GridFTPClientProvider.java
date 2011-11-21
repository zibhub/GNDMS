package de.zib.gndmc.util;
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

import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.util.GlobusCredentialProvider;
import de.zib.gndms.gritserv.util.GlobusCredentialProviderImpl;
import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author try ma ik jo rr a zib
 * @date 07.06.11  16:14
 * @brief
 */
public class GridFTPClientProvider {

    private String sourceURI;
    private String path;


    public GridFTPClient provideClient() throws IOException, ServerException, GlobusCredentialException, GSSException {

        GridFTPClient clnt = null;
        try {

            clnt = new GridFTPClient( getSourceURI( ), 2811 );
            clnt.authenticate( new GlobusGSSCredentialImpl( getCredential(), GSSCredential.DEFAULT_LIFETIME) );
            // ft.setFiles( getORQArguments().getFileMap() );
            // estimatedTransferSize = ft.estimateTransferSize(  );
            return clnt;
        } finally {
            if ( clnt != null )
                try {
                    clnt.close( true ); // none blocking close op
                } catch ( IOException e ) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch ( ServerException e ) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }

    }



    public GlobusCredential getCredential() throws GlobusCredentialException {
        return DelegationAux.findCredential( DelegationAux.defaultProxyFileName( System.getenv().get( "UID" ) ) );
    }


   public String getSourceURI() {
        return sourceURI;
    }


    public void setSourceURI( String sourceURI ) {
        this.sourceURI = sourceURI;
    }


    public String getPath() {
        return path;
    }


    public void setPath( String path ) {
        this.path = path;
    }
}
