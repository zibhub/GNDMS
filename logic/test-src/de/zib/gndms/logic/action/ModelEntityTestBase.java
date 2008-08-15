package de.zib.gndms.logic.action;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import java.io.File;
import java.util.Properties;
import static javax.persistence.Persistence.createEntityManagerFactory;

/**
 * A base class class for model tests.
 *
 * To uses this class provide the path to the home of the data-base and optional
 * the name of the relation to use (it defaults to "c3grid"). Then simply call
 * getEntityManager ( ) to obtain the entity manager.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 15.08.2008, Time: 12:38:08
 */
public class ModelEntityTestBase {

    private EntityManagerFactory emf;
    private EntityManager entityManager;
    private String dbPath;
    private String schemaName = new String( "c3grid" );

    public ModelEntityTestBase( ) {

    }


    public ModelEntityTestBase( String dbp ) {
        this.dbPath = dbp;
    }


    public ModelEntityTestBase( String dbp, String sn ) {
        this.dbPath = dbp;
        this.schemaName = sn;
    }


    public void tryCloseEMF( ) {

        if( emf != null && emf.isOpen( ) )
            emf.close( );
    }


    public EntityManagerFactory getEntityManagerFactory( ) {

        if( emf == null ) {
            createEMF( );
        }

        return emf;
    }


    public EntityManager getEntityManager() {

        if( entityManager == null ) {
            createEntityManager( );
        }

        return entityManager;
    }
    

    public void setEntityManager( EntityManager entityManager ) {
        this.entityManager = entityManager;
    }


    public String getDbPath() {
        return dbPath;
    }


    public void setDbPath( String dbp ) {
        this.dbPath = dbp;
    }


    public String getSchemaName() {
        return schemaName;
    }


    public void setSchemaName( String sn ) {
        this.schemaName = schemaName;
    }
    

    private void createEMF( ) {

        if( dbPath == null )
            throw new IllegalStateException( "No data base address provided." );

        if( schemaName == null )
            throw new IllegalStateException( "No data base schema name provided." );

        if( emf != null )
            throw new IllegalStateException( "Entity manager facotry already created." );

        File dbDir = new File( dbPath );

        if( ! dbDir.exists( ) )
            dbDir.mkdirs( );

        assert dbDir.isDirectory();

        try {
            System.setProperty("derby.system.home", dbDir.getCanonicalPath());
        } catch ( Exception e ) {
            throw new IllegalStateException( "Can't access derby.system.home" );
        }

        final Properties map = new Properties();

        map.put( "openjpa.Id", schemaName );
        map.put( "openjpa.ConnectionURL", "jdbc:derby:" +schemaName+ ";create=true");

        emf = createEntityManagerFactory( schemaName, map);
        System.out.println( System.getProperty( "derby.system.home" ) );
        assert emf != null;
    }


    public void createEntityManager( ) {

        if( entityManager != null )
            throw new IllegalStateException( "Entity manager already created." );

        if( emf == null )
            createEMF( );

        entityManager = emf.createEntityManager(  );

        assert entityManager != null;
    }
}
