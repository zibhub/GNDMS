/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.gndmc.dspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

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

    @BeforeClass( dependsOnGroups = "jpa" )
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

            emf = createEntityManagerFactory( "gndms", map );
        }
    }

    @AfterClass( dependsOnGroups = "jpa" )
    @Parameters( { "gridPath" } )
    public void cleanup( String gridPath ) {
        try {
            emf.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
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
