package de.zib.gndms.gritserv.util;

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



import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import org.apache.log4j.Logger;
import org.globus.ftp.GridFTPClient;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;

import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 10:43:34
 */
public class GlobusCredentialProviderImpl extends GlobusCredentialProvider {

    private static java.util.HashMap<String, CredentialInstaller> installers;
    private String key;
    private Logger logger = Logger.getLogger( this.getClass() );


    static {
        installers = new HashMap<String, CredentialInstaller >();
	CredentialInstaller ci = new GridFTPCredentialInstaller();
        installers.put( GORFXConstantURIs.FILE_TRANSFER_URI, ci );
        installers.put( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI, ci );
    }


    public GlobusCredentialProviderImpl() {
    }


    public GlobusCredentialProviderImpl( String tgt, GlobusCredential cred ) {
        setCredential( cred );
        key = tgt;
    }


    public void installCredentials( Object o ) {
        logger.debug( "target: " + key + " object: " + o.getClass().getName() );
        installers.get( key ).installCredentials( o, getCredential() );
    }


    public String getKey() {
        return key;
    }


    public void setKey( String key ) {
        this.key = key;
    }


    public static CredentialInstaller registerInstaller( String key, CredentialInstaller inst ) {
        return installers.put( key, inst );
    }


    public interface CredentialInstaller {
        public void installCredentials( Object o, GlobusCredential cred  );
    }

    static class GridFTPCredentialInstaller implements CredentialInstaller {
        public void installCredentials( Object o, GlobusCredential cred ) {
            GridFTPClient cnt = GridFTPClient.class.cast( o );
            try {
                cnt.authenticate(  new GlobusGSSCredentialImpl( cred, GSSCredential.DEFAULT_LIFETIME) );
            } catch ( Exception e ) {
                e.printStackTrace(); 
                throw new RuntimeException( e );
            }
        }
    }
}
