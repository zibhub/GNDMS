package de.zib.gndms.logic.model.gorfx.permissions;

import de.zib.gndms.kit.configlet.Configlet;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.model.common.types.PermissionConfigData;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;
import java.util.Properties;

/**
 * A class to configure permissions of files created by the GORFX service.
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
        deserializeConfig( data );
    }


    public void update( @NotNull Serializable data ) {
        deserializeConfig( data );
    }


    private void deserializeConfig(  @NotNull Serializable data ) {

        try{
            config = PermissionConfigData.class.cast( data );
        } catch ( ClassCastException e ) {
            config = new PermissionConfigData();
            config.fromProperties( ( Properties ) data );
        }
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


    /** This method always returns vaild permissions with respect to the current user mode.
     *
     * @param un The username to request the permissoin for. The username is only taken in account
     *          if the mode is Caller, in any other cases it can be null.
     */
    public FilePermissions permissionsFor( String un ) {

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


    /**
     * Returns the actual user name.
     *
     * The returened value depends on the the userMode setting of the
     * permission config. If it is DEFAULT the user which ownes the
     * container will be returned. If it is SINGLE the configured
     * single user name will be delivered and if is caller the
     * provided name (<em>un</em>) will be returned. If ths name is
     * <tt>null</tt>, the SINLGE user name will be returned if not
     * <tt>null</tt>,, else the DEFAULT user will be returned.
     * 
     *
     * @param un The name of the calling user or null if here is none.
     * @return The actual user name determined by the heuristic
     * mentioned above.
     */
    public String actualUserName( String un ) {

        switch( config.getMode( ) ) {
            case DEFAULT:
                return currentUser();
            case SINGLE:
                return singleUserOrFallback();
            case CALLER:
                return callerOrFallback( un );
            default:
                return currentUser();
        }
    }


    private String currentUser( ) {
        return System.getProperty( "user.name" );
    }


    private String singleUserOrFallback( ) {

        if( config.getSingleUser() != null )
            return config.getSingleUser();
        else
            return currentUser();
    }


    private String callerOrFallback( String un ) {

        if( un == null )
            singleUserOrFallback();
        
        return un;
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
