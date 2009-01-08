package de.zib.gndms.model.common;

import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.persistence.Column;

/**
 * A class to store information about task permissions. Usually instanziated in the middleware when creating the task.
 * It also privides access to the PermissionConfiglet.
 *
 * @author Maik Jorra <jorra@zib.de>
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

    private static de.zib.gndms.stuff.configlet.Configlet permissionConfiglet;


    public PermissionInfo( ) {

    }


    public PermissionInfo( String userName ) {
        this.userName = userName;
    }


    public PermissionInfo( PermissionInfo pi ) {
        this.userName = pi.userName;
    }


    @Column( name="user_name", nullable=true )
    public String getUserName() {
        return userName;
    }


    public void setUserName( String userName ) {
        this.userName = userName;
    }


    @Transient
    public static de.zib.gndms.stuff.configlet.Configlet getPermissionConfiglet() {
        return permissionConfiglet;
    }


    public static void setPermissionConfiglet( de.zib.gndms.stuff.configlet.Configlet permissionConfiglet ) {
        PermissionInfo.permissionConfiglet = permissionConfiglet;
    }
}
