package de.zib.gndms.logic.action;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static javax.persistence.Persistence.createEntityManagerFactory;
import java.io.File;
import java.util.Properties;

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
public abstract class ModelEntityTestBase {

    private EntityManagerFactory emf;
    
    private EntityManager entityManager;
    private String dbPath;
    private String dbName = "c3grid";


    public ModelEntityTestBase( ) {

    }


    public ModelEntityTestBase( String dbp ) {
        this.dbPath = dbp;
    }

    @Parameters({ "dbPath", "dbName" })
    public ModelEntityTestBase( String dbPath, @Optional("c3grid") String Name ) {
        this.dbPath = dbPath;
        this.dbName = Name;
    }


    public void removeDbPath() {
        erasePath(new File(dbPath));
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
            setupSharedEntityManager( );
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


    public String getDbName() {
        return dbName;
    }


    public void setDbName( String sn ) {
        this.dbName = dbName;
    }
    

    private void createEMF( ) {

        if( dbPath == null )
            throw new IllegalStateException( "No data base address provided." );

        if( dbName == null )
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

        map.put( "openjpa.Id", dbName);
        map.put( "openjpa.ConnectionURL", "jdbc:derby:" + dbName + ";create=true");

        emf = createEntityManagerFactory(dbName, map);
        System.out.println( System.getProperty( "derby.system.home" ) );
        assert emf != null;
    }


    public void setupSharedEntityManager( ) {

        if( entityManager != null )
            throw new IllegalStateException( "Entity manager already created." );

        if( emf == null )
            createEMF( );

        entityManager = emf.createEntityManager(  );

        assert entityManager != null;
    }


    public static void erasePath(final @NotNull File path) {
        if (path.exists() && path.isDirectory())
			rmDirRecursively(path);
    }


    public static void rmDirRecursively(File fileParam) {
        for (File file : fileParam.listFiles()) {
            if (file.isDirectory())
                rmDirRecursively(file);
            else
                file.delete();
        }
        fileParam.delete();
	}
}
