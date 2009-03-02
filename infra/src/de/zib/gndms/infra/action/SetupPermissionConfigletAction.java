package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.gorfx.permissions.PermissionConfiglet;
import de.zib.gndms.model.common.ConfigletState;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;

import javax.persistence.EntityManager;
import java.io.*;
import java.util.Properties;

/**
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 09.01.2009, Time: 14:53:12
 */
@ConfigActionHelp(shortHelp = "Setup the permission configlet in the database")
public class SetupPermissionConfigletAction extends SetupConfigletAction {

    private static final String NAME = "PermissionConfiglet";
    private static final Class CLAZZ = PermissionConfiglet.class;

    @ConfigOption( descr = "Properties describing the permissions" )
    private String permissionProperties;


    public SetupPermissionConfigletAction( ) {
        setName( NAME );
        setClassName( CLAZZ.getName() );
    }



    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if ( SetupMode.CREATE.equals( getMode() )
            || SetupMode.UPDATE.equals( getMode() ) )
        {
            if( !hasOption( "permissionProperties" ) )
                throw new IllegalStateException( "Permission properties are required for creation and update" );
        }
    }


    @Override
    protected ConfigActionResult read( ConfigletState state, EntityManager emParam, PrintWriter writerParam ) {

        Properties prop = ( Properties ) state.getState( );
        ByteArrayOutputStream os = new ByteArrayOutputStream( );
        try {
            prop.store( os, NAME );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }

        writerParam.print( os.toString( ) );

        return ok();
    }


    @Override
    protected ConfigActionResult create( ConfigletState state, EntityManager emParam, PrintWriter writerParam ) {

        state.setState( asProperties() );
        return ok(  );
    }


    @Override
    protected ConfigActionResult update( ConfigletState state, EntityManager emParam, PrintWriter writerParam ) {

        Properties op = (Properties) state.getState( );
        Properties np = asProperties();

        for( Object k : np.keySet( ) ) {
            String key = (String) k;
            op.setProperty( key, np.getProperty( key ) );
        }

        state.setState( op );
        
        return ok( );
    }

    /**
     * Returns {@code permissionProperties}. If not set using {@code setPermissionProperties},
     * it will try to retrive it from the value set for the option {@code permissionProperties} in the configuration map.
     *  
     * @return
     */
    public String getPermissionProperties() {

        if( permissionProperties == null ) {
            try{
                permissionProperties = getOption( "permissionProperties" ).replaceAll( "\\s+__EOL__\\s*", "\n");
            } catch ( MandatoryOptionMissingException e ) {
                // can be safely ignored, we wouldn't have come this far w/o permissionProperties
            }
        }

        return permissionProperties;
    }


    public void setPermissionProperties( String permissionProperties ) {
        this.permissionProperties = permissionProperties;
    }


    /**
     * Returns the {@code permissionProperties} as {@code Properties}
     * @return the {@code permissionProperties} as {@code Properties}
     */
    private Properties asProperties(  ) {

        Properties prop = new Properties( );
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream( getPermissionProperties().getBytes( "ISO8859_1" ) );
            prop.load( bis );
            bis.close( );
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException( e );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }

        return prop;
    }
}
