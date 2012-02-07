/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.kit.security;

import de.zib.gndms.kit.access.GNDMSBinding;
import de.zib.gndms.kit.util.DirectoryAux;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.ietf.jgss.GSSCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
* @author Maik Jorra
* @email jorra@zib.de
* @date 01.12.11  19:00
* @brief An installer, which writes a credential to disk.
*/
public class AsFileCredentialInstaller extends GSSCredentialInstaller<File> {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );
    private DirectoryAux directoryAux = GNDMSBinding.getInjector().getInstance( DirectoryAux.class );


    public AsFileCredentialInstaller() {
        setReceiverClass( File.class );
    }


    public void installCredentials( File destFile, GSSCredential cred ) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream( destFile );
            fos.write( ( ( ExtendedGSSCredential ) cred ).export( ExtendedGSSCredential.IMPEXP_OPAQUE ) );
            fos.close();
            int ret = directoryAux.chmod( 0600, destFile );
            if( ret != 0 )
                throw new IllegalStateException( "chmod returned "+ ret );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}
