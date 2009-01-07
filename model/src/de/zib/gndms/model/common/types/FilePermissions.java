package de.zib.gndms.model.common.types;

import de.zib.gndms.model.common.AccessMask;

import java.io.Serializable;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 22.12.2008, Time: 13:12:37
 */
public class FilePermissions implements Serializable {
    // todo add serialVersionUId


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
}

