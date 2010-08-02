package de.zib.gndmc.GORFX.beans;

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



import de.zib.gndmc.util.LoadablePropertyBean;
import de.zib.gndms.gritserv.delegation.DelegationAux;

import java.util.Properties;

/**
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:35:52
 */
public class GorfxBean extends LoadablePropertyBean {

    public static final String GORFX_URI_KEY = "GORFX.serviceURI" ;
    public static final String USE_DELEGATION_KEY = "GORFX.delegation.disable" ;
    public static final String DELEGATION_UID_KEY = "GORFX.delegation.uid" ;
    // or
    public static final String DELEGATION_PROXY_FILE_KEY = "GORFX.delegation.proxyFile";

    private boolean disableDelegation = false;
    private Integer uid = null;
    private String proxyFile;

    private String gorfxURI;


    public void setProperties( Properties prop ) {
        gorfxURI = prop.getProperty( GORFX_URI_KEY );
        if( prop.containsKey( USE_DELEGATION_KEY ) )
            disableDelegation = Boolean.parseBoolean( prop.getProperty( USE_DELEGATION_KEY ) );

        if( prop.containsKey( DELEGATION_UID_KEY ) )
            uid = Integer.parseInt( prop.getProperty( DELEGATION_UID_KEY ) ) ;
        else if( prop.contains( DELEGATION_PROXY_FILE_KEY ) )
            proxyFile = prop.getProperty( DELEGATION_PROXY_FILE_KEY );
    }


    public void createExampleProperties( Properties prop ) {
        prop.setProperty( GORFX_URI_KEY, "<the-uri-of-the-GORFX-service>" );
        prop.setProperty( USE_DELEGATION_KEY, "<disables-delegation-if-set-to-TRUE-(Don't do it)>" );
        prop.setProperty( DELEGATION_UID_KEY, "<INTEGER-uid-of-the-user-Works-with-standard-proxyfile>" );
        prop.setProperty( DELEGATION_PROXY_FILE_KEY, "<Path-to-the-proxy-file-If-.uid-isn't-used>" );
    }


    public String getGorfxURI() {
        return gorfxURI;
    }


    public void setGorfxURI( String gorfxURI ) {
        this.gorfxURI = gorfxURI;
    }


    public boolean isDisableDelegation() {
        return disableDelegation;
    }


    public void setDisableDelegation( boolean disableDelegation ) {
        this.disableDelegation = disableDelegation;
    }


    public int getUid() {
        return uid;
    }


    public void setUid( int uid ) {
        this.uid = uid;
    }


    public String getProxyFile() {
        
        if( proxyFile == null && uid != null )
            return DelegationAux.defaultProxyFileName( String.valueOf( uid ) );

        return proxyFile;
    }


    public void setProxyFile( String proxyFile ) {
        this.proxyFile = proxyFile;
    }
}
