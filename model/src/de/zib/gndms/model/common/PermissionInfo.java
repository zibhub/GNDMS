package de.zib.gndms.model.common;

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



import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * A class to store information about task permissions. Usually instantiated in the middleware when creating the task.
 * It also provides access to the PermissionConfiglet.
 *
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 06.01.2009, Time: 13:58:52
 *
 * @note Currently only the username is stored, later this place can and should be used to store more advanced infos
 *       like the certificate information and the like.
 */
@Embeddable
@Copyable( CopyMode.CONSTRUCT )
public class PermissionInfo {

    private String userName; // Name of the user who is creating the task.
                             // If this one is null it means that the configlet should be queried.

    // The name of the configlet whose permission set should be applied.
    private String permissionConfigletName;


    public Serializable getCredential() {
        return credential;
    }


    public void setCredential( final Serializable credential ) {
        this.credential = credential;
    }


    private Serializable credential;


    public PermissionInfo( ) {

    }


    public PermissionInfo( String userName ) {
        this.userName = userName;
    }


    public PermissionInfo( String cn, String config ) {

        this.userName = cn;
        permissionConfigletName = config;
    }
    

    public PermissionInfo( PermissionInfo pi ) {
        this.userName = pi.userName;
        this.permissionConfigletName = pi.permissionConfigletName;
    }




    @Column( name="user_name", nullable=true )
    public String getUserName() {
        return userName;
    }


    public void setUserName( String userName ) {
        this.userName = userName;
    }


    @Column( name="permission_config", nullable=true )
    public String getPermissionConfigletName() {
        return permissionConfigletName;
    }


    public void setPermissionConfigletName( String permissionConfigletName ) {
        this.permissionConfigletName = permissionConfigletName;
    }
}
