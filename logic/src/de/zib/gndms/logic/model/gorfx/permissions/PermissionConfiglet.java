package de.zib.gndms.logic.model.gorfx.permissions;

import de.zib.gndms.stuff.configlet.Configlet;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.model.common.types.PermissionConfigData;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;

/**
 * A class to configure the permissions of files created by the GORFX service.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.12.2008, Time: 11:00:25
 */
public class PermissionConfiglet implements Configlet {

    private Log logger;
    private String name;
    private PermissionConfigData config;

    public void init( @NotNull Log loggerParam, @NotNull String name, Serializable data ) {
        logger = loggerParam;
        this.name = name;
        config = PermissionConfigData.class.cast( data );
    }


    public void update( @NotNull Serializable data ) {
        config = PermissionConfigData.class.cast( data );
    }


    public void shutdown() {
        // Not required here
    }


    public PermissionConfigData.UserMode getMode() {
        return config.getMode();
    }


    public FilePermissions getPermissions() {
        return config.getDefaultPermissions();
    }


    /** This method always returns vaild permissions which respect the current user mode.
     *
     * @param un The username to request the permissoin for. The username is only taken in account
     *          iff the mode is Caller, in any other cases it can be null.
     */
    public FilePermissions getPermissionsFor( String un ) {

        switch ( config.getMode() ) {
            case DEFAULT:
                logger.debug( "Return permissions for default user" );
                return config.getDefaultPermissions( );
            case SINGLE:
                return tryGetPermissions( config.getSingleUser() );
            case CALLER:
                return tryGetPermissions( un );
            default:
                throw new IllegalStateException( "invalid enum value occured" );
        }
    }


    private FilePermissions tryGetPermissions( String un ) {

        if( config.getPerUserPermissions().containsKey( un ) ) {
            logger.debug( "Return permissions for user " + un );
            return config.getPerUserPermissions().get( un );
        } else {
            logger.debug( "Return default permissions for user " + un );
            return config.getDefaultPermissions();
        }
    }
}
