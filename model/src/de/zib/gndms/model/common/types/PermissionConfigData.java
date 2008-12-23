package de.zib.gndms.model.common.types;

import java.io.Serializable;
import java.util.Map;

/**
 * The setting here are considered as recommendation an will applied only if possible.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 23.12.2008, Time: 12:40:13
 */
public class PermissionConfigData implements Serializable {
    private static final long serialVersionUID = 4015833130066124297L;

    public enum UserMode {
        DEFAULT, // the owner of the container
        SINGLE,  // use a single user which is different for the container
        CALLER   // the caller of the service i.e. the cert owner (intended for later use)
    }

    private UserMode mode; // The mode for permission handling.
    private FilePermissions defaultPermissions; // The default file access mask.

    private String singleUser; // name of the single user, only required when user-mode is set to SINGLE.

    private Map<String, FilePermissions> perUserPermissions; // This set is requried in the case that mode is caller.
                                                    // it offers the possibility to set different permissions for each
                                                    // user.


    public UserMode getMode() {
        return mode;
    }


    public void setMode( UserMode mode ) {
        this.mode = mode;
    }


    public FilePermissions getDefaultPermissions() {
        return defaultPermissions;
    }


    public void setDefaultPermissions( FilePermissions defaultPermissions ) {
        this.defaultPermissions = defaultPermissions;
    }


    public String getSingleUser() {
        return singleUser;
    }


    public void setSingleUser( String singleUser ) {
        this.singleUser = singleUser;
    }


    public Map<String, FilePermissions> getPerUserPermissions() {
        return perUserPermissions;
    }


    public void setPerUserPermissions( Map<String, FilePermissions> perUserPermissions ) {
        this.perUserPermissions = perUserPermissions;
    }
}
