/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zib.adis.abi;

import de.zib.adis.Role;
import de.zib.adis.Type;
import de.zib.vold.client.RESTClient;
import de.zib.vold.common.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @date: 02.04.12
 * @time: 15:13
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class GLUEDis extends ABIi {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private RESTClient voldi;
    private String dir;

    public GLUEDis() {
        this.voldi = new RESTClient();
        this.voldi.setEnc( "utf-8" );
        this.dir = "c3grid/glue13";
    }

    /**
     * Set the URL to the VolD service.
     * @param voldURL This string should look like http://ip.address.de/VolD/master
     */
    public void setVoldURL( String voldURL ) {
        voldi.setBaseURL(voldURL);
    }

    /**
     * Set the encoding used to store data.
     * @param enc utf-8 should be a good choice here.
     */
    public void setEnc( String enc ) {
        voldi.setEnc( enc );
    }

    /**
     * Set the name of the grid in which the GNDMS is used.
     * @param grid For C3-Grid this should be c3grid
     */
    public void setDir( String dir ) {
        this.dir = dir;
    }

    public void checkState() {
        voldi.checkState();

        if( null == dir ) {
            throw new IllegalStateException( "Tried to operate on ADiS while it had not been initialized yet. Set proper gridname before!" );
        }
    }

    private Collection<String> listValuesByTypeAndKey( String type, String key ) {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( dir, type, key ) );

        if( null == _result )
            return null;

        return flatten(_result.values());
    }

    /**
     * Get the number of running jobs at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number of running jobs.
     */
    public Integer getGlueCEStateRunningJobs( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueCEStateRunningJobs";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    /**
     * Get the number of total jobs at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number of total jobs.
     */
    public Integer getGlueCEStateTotalJobs( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueCEStateTotalJobs";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    /**
     * Get the number of physical cpus at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number of physicak cpus.
     */
    public Integer getGlueSubClusterPhysicakCPUs( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueSubClusterPhysicalCPUs";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    /**
     * Get the number of logical cpus at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number of logical cpus.
     */
    public Integer getGlueSubClusterLogicalCPUs( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueSubClusterLogicalCPUs";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    /**
     * Get the number of free job slots at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number free job slots.
     */
    public Integer getGlueCEStateFreeJobSlots( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueCEStateFreeJobSlots";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    /**
     * Get the number of waiting jobs at the gram endpoint.
     * @param gram The gram endpoint.
     * @return The number waiting jobs.
     */
    public Integer getGlueCEStateWaitingJobs( String gram ) {
        // guard
        {
            checkState();
        }

        final String type = "GlueCEStateWaitingJobs";

        Collection< String > _result = listValuesByTypeAndKey( type, gram );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 != _result.size() ) {
            log.warn( "More or less than one " + type + "  registered!" );
        }

        return Integer.valueOf( _result.iterator().next() );
    }

    private Map<Key, Set<String>> simplemap( Key key, String value ) {
        Map<Key, Set<String>> result = new HashMap<Key, Set<String>>();

        Set<String> set = new HashSet<String>();
        set.add( value );

        result.put( key, set );

        return result;
    }

    private Set<String> simpleset( String s ) {
        Set<String> set = new HashSet<String>();

        set.add( s );

        return set;
    }

    private Map<String, String> flatmap( Map<Key, Set<String>> map ) {
        Map<String, String> result = new HashMap<String, String>();

        for( Map.Entry<Key, Set<String>> entry : map.entrySet() ) {
            if( 1 != entry.getValue().size() ) {
                log.warn( "Unexpected state in database: got " + entry.getValue().size() + " values for " + entry.getKey().toString() + "." );
                continue;
            }

            result.put( entry.getKey().get_keyname(), entry.getValue().iterator().next() );
        }

        return result;
    }

    private Set<String> flatten( Collection<Set<String>> setset ) {
        Set<String> result = new HashSet<String>();

        for( Set<String> set : setset ) {
            result.addAll( set );
        }

        return result;
    }

    private Set<String> usualset( Set<Key> set ) {
        Set<String> result = new HashSet<String>();

        for( Key k : set ) {
            result.add( k.get_keyname() );
        }

        return result;
    }
}
