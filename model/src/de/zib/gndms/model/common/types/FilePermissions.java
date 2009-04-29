package de.zib.gndms.model.common.types;

import de.zib.gndms.model.common.AccessMask;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * A FilePermissions class is used to controll access rights for a file.
 *
 * @see AccessMask
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


