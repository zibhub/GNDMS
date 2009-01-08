package de.zib.gndms.model.common.types;

import de.zib.gndms.stuff.propertytree.PropertyTree;
import de.zib.gndms.stuff.propertytree.PropertyTreeFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        SINGLE,  // use a single user which is different for the containers owner
        CALLER   // the caller of the service i.e. the cert owner (intended for later use)
    }

    private UserMode mode; // The mode for permission handling.
    private FilePermissions defaultPermissions; // The default file access mask.
                                                // The meaning of group and name differ from mode to mode.

    private String singleUser; // name of the single user, only required when user-mode is set to SINGLE.

    private Map<String, FilePermissions> perUserPermissions; // This set is required in the case that mode is caller.
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

    
    public void addUserPermissions( String usr, FilePermissions perm ) {
        if( perUserPermissions == null )
            perUserPermissions = new HashMap<String, FilePermissions>( );

        perUserPermissions.put( usr, perm );
    }


    /**
     * Loads properties from File.
     *
     * Properties are required to start with PermissionConfig. The following properties are supported
     * <pre>
     * userMode={DEFAULT|SINGLE|CALLER} (default:DEFAULT)
     * defaultPermissions.user=USR_NAME     # The name here isn't imported the other attributes matter
     * defaultPermissions.group=GRP_NAME
     * defaultPermissions.mask=UGO  (default=600)
     * singleUserName=USR_NAME
     * userPermissions=USR_NAME_0 ... USR_NAME_n
     * userPermissions.USR_NAME_i.user=USR_NAME
     * userPermissions.USR_NAME_i.group=GRP_NAME
     * userPermissions.USR_NAME_i.mask=UGO
     *              ( with i in [0;n] )
     *
     * USR_NAME := [a-z_][a-z0-9_]*
     * GRP_NAME := ([a-z_][a-z0-9_]*){,16}
     * UGO      := [0-7]{3}
     * USR_NAME_i := USR_NAME
     * </pre>
     *
     * @param f The File to read from.
     * @throws IOException whenever suitable
     */
    public void fromPropertyFile( File f ) throws IOException {

        PropertyTree props = PropertyTreeFactory.createPropertyTree( f );

        mode = UserMode.valueOf( props.getProperty( "PermissionConfig.userMode", "DEFAULT" ) );

        PropertyTree fp = props.subTree( "PermissionConfig.defaultPermissions" );

        defaultPermissions = readPermissions( fp, System.getProperty( "user.name" ), null, "600" );

        String usr = props.getProperty( "PermissionConig.singleUserName" );

        if( usr != null ) {
            checkUserName( usr );
            singleUser = usr;
        }

        String usrkey =  "PermissionConfig.userPermissions";
        String users = props.getProperty( usrkey );
        usrkey += ".";
        if( users != null ) {
            String[] usrl = users.split( "\\s+" );
            List<String> sl = Arrays.asList( usrl );
            for( String s : sl ) {
                PropertyTree pt = props.subTree( usrkey + s );
                if( pt != null ) {
                    FilePermissions ufp = readPermissions( pt, null, null, "600" );
                    if( ufp != null )
                        addUserPermissions( s, ufp );
                    else {
                        throw new IllegalStateException( "No permissions given for user: " + ufp );
                    }
                }
            }
        }
    }


    /**
     * @param pt property tree with file permissions.
     * @param un Default value for user name.
     * @param gn Default value for group name.
     * @param msk Default access mask.
     *
     * @return The read file permissions.
     */
    private FilePermissions readPermissions( PropertyTree pt, String un, String gn, String msk ) {

        if( pt == null )
            return null;

        String usr = pt.getProperty( "user", un );
        checkUserName( usr );

        String grp = pt.getProperty( "group", gn );
        if( grp != null )
            checkGroupName( grp );

        String perm = pt.getProperty( "mask", msk);

        return new FilePermissions( usr, grp, perm );
    }


    private void checkUserName( String usr ) {

        final Pattern pat_usr = Pattern.compile( "[a-z_][a-z0-9_]*" );
        Matcher m = pat_usr.matcher( usr );
        if( ! m.matches() )
            throw new IllegalArgumentException( usr + "is not a vaild user name" );
    }


    private void checkGroupName( String grp ) {
        final Pattern pat_grp = Pattern.compile( "([a-z_][a-z0-9_]*){1,16}" );

        Matcher m = pat_grp.matcher( grp );
        if( !m.matches() )
            throw new IllegalArgumentException( grp + "is not a vaild group name" );
    }


    @Override
    public String toString() {
        return "PermissionConfigData{" +
            "mode=" + mode +
            ", defaultPermissions=" + defaultPermissions +
            ", singleUser='" + singleUser + '\'' +
            ", perUserPermissions=" + perUserPermissions +
            '}';
    }
}
