package de.zib.gndms.gndmc.dspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static javax.persistence.Persistence.createEntityManagerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: explicit
 * Date: 02.12.11
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class JPATest {
    protected final Logger log = LoggerFactory.getLogger( this.getClass() );
    protected EntityManagerFactory emf;
    //private String gridPath;

    @BeforeMethod( dependsOnGroups = "jpa" )
    @Parameters( { "gridPath" } )
    public void init( String gridPath ) {
        // cleanup first
        {
            try {
                deleteDirectory( new File( gridPath ) );
            }
            catch( Exception e ) {
                log.error( "Could not delete " + gridPath + ". " + e.getMessage() );
            }
        }

        // create entity manager factory
        {
            System.setProperty( "derby.system.home", gridPath );

            final Properties map = new Properties();

            map.put( "openjpa.Id", "testid" );
            map.put( "openjpa.ConnectionURL", "jdbc:derby:TESTDB;create=true" );

            log.info( "Opening JPA Store: " + map.toString() );

            emf = createEntityManagerFactory( "testdbunit", map );
        }
    }

    @AfterMethod( dependsOnGroups = "jpa" )
    @Parameters( { "gridPath" } )
    public void cleanup( String gridPath ) {
        emf.close();
    }

    private static void deleteDirectory( File path ) throws IOException {
        if( path.isDirectory() ) {
            for( File child: path.listFiles() ) {
                deleteDirectory(child);
            }
        }
        if( !path.delete() ) {
            throw new IOException( "Could not delete " + path );
        }
    }
}
