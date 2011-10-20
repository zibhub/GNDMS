package de.zib.gndms.model.common.types;

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



import de.zib.gndms.common.model.common.AccessMask;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * The FilePermissions class is used to control access rights for a file.
 * An instance corresponds to one user and one file. It contains the {@link AccessMask},
 * describing the concrete access rights, the name of the user and the name of the group, the user belongs to.
 * 
 * @see AccessMask
 * @author  try ma ik jo rr a zib
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 22.12.2008, Time: 13:12:37
 */
public class FilePermissions implements Serializable {

    private static final long serialVersionUID = 6117137011878526291L;

    private String user;
    private String group;
    private AccessMask accessMask;


    public FilePermissions( ) {

    }


    public FilePermissions( String owner, String group, String accessMask ) {
        this.user = owner;
        this.group = group;
        this.accessMask = AccessMask.fromString( accessMask );
    }


    public String getUser() {
        return user;
    }


    public void setUser( String user ) {
        this.user = user;
    }


    public String getGroup() {
        return group;
    }


    public void setGroup( String group ) {
        this.group = group;
    }


    public AccessMask getAccessMask() {
        return accessMask;
    }


    public void setAccessMask( AccessMask accessMask ) {
        this.accessMask = accessMask;
    }


    /**
     * Returns a String in format
     * <pre>
     *      FilePermissions{user='$user', group='$group', acesssMask=$accessMask}
     *
     * where
     *      $user is the username
     *      $group is the groupname
     *      $accessmask is the accessMask in decimal representation
     * 
     * @return a String showing the username, groupname and access mask
     */
    @Override
    public String toString() {
        return "FilePermissions{" +
            "user='" + user + '\'' +
            ", group='" + group + '\'' +
            ", accessMask=" + accessMask +
            '}';
    }


    /**
     * Puts the username, groupname and mask to {@code moreProps}.
     *
     * @param prefix a String can be denoted, which will be prefix for the new keys of {@code moreProps}.
     * 
     * @param moreProps the {@code Properties} instance, the values will be appended to.
     */
    public void toProperties( String prefix, Properties moreProps ) {

        final String pre = prefix != null ? prefix + "." : "" ;
        moreProps.setProperty( pre + "user", user );
        moreProps.setProperty( pre + "group", group );
        moreProps.setProperty( pre + "mask", accessMask.toString() );

    }
}


