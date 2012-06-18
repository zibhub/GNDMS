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

package de.zib.gndms.voldmodel;

import de.zib.gndms.voldmodel.abi.ABIi;
import de.zib.vold.client.VolDClient;
import de.zib.vold.common.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This is the C3-Grid VolD Client.
 *
 * VolD will be used as the MDS replacement in the new C3-Grid. This class is
 * the client used to store and get data from the VolD storage.
 */
public class Adis extends ABIi {
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private VolDClient voldi;
    private String grid;

    public Adis() {
        this.voldi = new VolDClient();
        this.voldi.setEnc( "utf-8" );
        this.grid = "c3grid";
    }

    /**
     * Set the URL to the VolD service.
     * @param voldURL This string should look like http://ip.address.de/VolD/master
     */
    public void setVoldURL( String voldURL ) {
        voldi.setBaseURL( voldURL );
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
    public void setGrid( String grid ) {
        this.grid = grid;
    }

    public void checkState() {
        voldi.checkState();

        if( null == grid ) {
            throw new IllegalStateException( "Tried to operate on ADiS while it had not been initialized yet. Set proper gridname before!" );
        }
    }

    private boolean checkRole( String role ) {
        for( Role r : Role.values() ) {
            if( r.name().equals( role ) )
                return true;
        }

        return false;
    }

    private boolean checkType( String type ) {
        for( Type t : Type.values() ) {
            if( t.name().equals( type ) )
                return true;
        }

        return false;
    }



    public String getRole( String role ) {
        // guard
        {
            checkState();

            checkRole( role );
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, role, "..." ) );

        if( null == _result ) {
            return null;
        }

        // should be exactly one entry!
        if( 1 < _result.size() ) {
            logger.warn("More than one " + role + " endpoint registered!");
        }

        // search first valid entry
        for( Map.Entry<Key, Set<String>> entry : _result.entrySet() ) {
            if( 0 == entry.getValue().size() ) {
                continue;
            }
            if( 1 < entry.getValue().size() ) {
                logger.warn("More than one " + role + " endpoint registered!");
            }

            return entry.getValue().iterator().next();
        }

        return null;
    }

    public Collection<String> listValuesByType( String type ) {
        // guard
        {
            checkState();

            checkType( type );
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, type, "..." ) );

        if( null == _result )
            return null;

        return flatten( _result.values() );
    }

    public Collection<String> listKeysByType( String type ) {
        // guard
        {
            checkState();

            checkType( type );
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, type, "..." ) );

        if( null == _result )
            return null;

        return usualset( _result.keySet() );
    }




    /**
     * Get the URL of the central data management system.
     * @return Endpoint to DMS.
     */
    public String getDMS() {
        return getRole( Role.DMS.toString() );
    }


    /**
     * Get the URL of the workflow scheduler system.
     * @return Endpoint to WSS.
     */
    public String getWSS() {
        return getRole( Role.WSS.toString() );
    }


    /**
     * List available compute providers.
     *
     * @return All registered compute provider ids.
     */
    public Collection<String> listCPs() {
        return listKeysByType( Type.CPID_GRAM.toString() );
    }


    /**
     * List available Harvester URLs.
     *
     * This method can be used by the portal to get all harvesters.
     *
     * @return Harvester endpoints.
     */
    public Collection<String> listOAIs() {
        return listValuesByType( Type.OAI.toString() );
    }



    /**
     * List all available import sites.
     * @return All import site URLs.
     */
    public Collection<String> listImportSites() {
        return listValuesByType( Type.IMPORT.toString() );
    }


    /**
     * List all available export sites.
     * @return All export site URLs.
     */
    public Map<String, String> listExportSites() {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, Type.EXPORT.toString(), "..." ) );

        if( null == _result ) {
            return null;
        }

        return flatmap( _result );
    }


    /**
     * List all available workflows.
     * @return All available workflows.
     */
    public Collection<String> listWorkflows() {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, Type.WORKFLOW.toString(), "..." ) );

        if( null == _result ) {
            return null;
        }

        return usualset( _result.keySet() );
    }


    /**
     * List all Dataprovider URLs hosting data with an OID prefix.
     * @param oidprefix
     * @return All URLs of the data providers hosting the data with the OID prefix oidprefix.
     */
    public Collection<String> listGORFXbyOID( String oidprefix ) {
        return listValuesByType( Type.OID.toString() );
    }


    /**
     * List all publisher sites.
     * @return All publisher sites.
     */
    public Map<String, String> listPublisher() {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, Type.PUBLISHER.toString(), "..." ) );

        if( null == _result ) {
            return null;
        }

        return flatmap( _result );
    }


    /**
     * Get all GRAM endpoints supporting the given workflow.
     * @param workflow
     * @return According GRAM endpoints.
     */
    public Map<String, String> getEPbyWorkflow( String workflow ) {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> _result = voldi.lookup( new Key( grid, Type.WORKFLOW.toString(), workflow ) );

        if( null == _result ) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>();

        // should be exactly one entry
        for( Map.Entry<Key, Set<String>> entry : _result.entrySet() ) {
            for( String gram : entry.getValue() ) {
                Map<Key, Set<String>> _gramres = voldi.lookup( new Key( grid, Type.GRAM.toString(), gram ) );

                if( null == _gramres ) {
                    logger.warn("Workflow " + workflow + " had a GRAM EndPoint " + gram + " registered, which has neither a GridFTP nor a SubSpace endpoint.");
                    continue;
                }

                result.putAll( flatmap( _gramres ) );
            }
        }

        return result;
    }



    public boolean setRole( String role, String endpoint ) {
        // guard
        {
            checkState();

            checkRole( role );
        }

        voldi.insert( null, simplemap( new Key( grid, role.toString(), "" ), endpoint ) );

        return true;
    }


    public boolean setType( String type, String name, String value ) {
        // guard
        {
            checkState();

            checkType( type );

            if( null == name ) {
                name = "";
            }
        }

        voldi.insert( null, simplemap( new Key( grid, type, name ), value ) );

        return true;
    }


    /**
     * Set the central data management system endpoint.
     * @param endpoint
     * @return true on success.
     */
    public boolean setDMS( String endpoint ) {
        return setRole( Role.DMS.toString(), endpoint );
    }


    /**
     * Set the workflow scheduler system endpoint.
     * @param endpoint
     * @return true on success.
     */
    public boolean setWSS( String endpoint ) {
        return setRole( Role.WSS.toString(), endpoint );
    }


    /**
     * Register an export site.
     *
     * This method should be called by a host running this export site.
     *
     * @param   name      Human readable name.
     * @param   subspace  Endpoint URL.
     * @return  true on success.
     */
    public boolean setExport( String name, String subspace ) {
        return setType( Type.EXPORT.toString(), name, subspace );
    }


    /**
     * Register an import site.
     *
     * This method should be called by the host running the import site.
     *
     * @param name          Human readable name.
     * @param subspace      Endpoint URL.
     * @return              true on success.
     */
    public boolean setImport( String name, String subspace ) {
        return setType( Type.IMPORT.toString(), name, subspace );
    }


    /**
     * Register a workflow.
     *
     * This method should be called by all compute providers supporting this workflow.
     *
     * @param subspace  Endpoint for all incoming and outgoing data.
     * @param cpId The compute provider id.
     * @param gram The gram endpoint (triple of gram server, job manager and queue name).
     * @param workflows The set of all workflows supported by this compute provider.
     * @return true on success.
     */
    public boolean setWorkflows(
            final String subspace,
            final String cpId,
            final String gram,
            final Collection<String> workflows
    ) {
        // guard
        {
            checkState();
        }

        final Map<Key, Set<String>> request = new HashMap<Key, Set<String>>();
        final Set<String> cpIdSet = simpleset( cpId );

        // add workflow |--> cpId
        for( String workflow : workflows ) {
            request.put( new Key( grid, Type.WORKFLOW.toString(), workflow ), cpIdSet );
        }

        // add cpId |--> gram
        request.put( new Key( grid, Type.CPID_GRAM.toString(), cpId ), simpleset( gram ) );

        // add cpId |--> subspace
        request.put( new Key( grid, Type.GRAM.toString(), cpId ), simpleset( subspace ) );

        voldi.insert( null, request );

        return true;
    }


    /**
     * Register a set of OID prefixes on a data provider.
     *
     * This method should be called by the data provider hosting the oid prefixes.
     *
     * @param gorfx Endpoint of the data provider.
     * @param oidprefixe
     * @return true on success.
     */
    public boolean setOIDPrefixe( String gorfx, Collection<String> oidprefixe ) {
        // guard
        {
            checkState();
        }

        Map<Key, Set<String>> request = new HashMap<Key, Set<String>>();

        // add prefix |--> gorfx
        for( String oidprefix : oidprefixe ) {
            request.put( new Key( grid, Type.OID.toString(), oidprefix ), simpleset( gorfx ) );
        }

        voldi.insert( null, request );

        return true;
    }


    /**
     * Register an OAI harvester.
     *
     * This method should be called by the data provider or portal running the harvester.
     *
     * @param endpoint
     * @return true on success.
     */
    public boolean setOAI( String endpoint ) {
        return setType( Type.OAI.toString(), "", endpoint );
    }




    private static Map<Key, Set<String>> simplemap( Key key, String value ) {
        Map<Key, Set<String>> result = new HashMap<Key, Set<String>>();

        Set<String> set = new HashSet<String>();
        set.add( value );

        result.put( key, set );

        return result;
    }


    private static Set<String> simpleset( String s ) {
        Set<String> set = new HashSet<String>();

        set.add( s );

        return set;
    }


    // could be static, but the logger...
    private Map<String, String> flatmap( Map<Key, Set<String>> map ) {
        Map<String, String> result = new HashMap<String, String>();

        for( Map.Entry<Key, Set<String>> entry : map.entrySet() ) {
            if( 1 != entry.getValue().size() ) {
                logger.warn("Unexpected state in database: got " + entry.getValue().size() + " values for " + entry.getKey().toString() + ".");
                continue;
            }

            result.put( entry.getKey().get_keyname(), entry.getValue().iterator().next() );
        }

        return result;
    }


    private static Set<String> flatten( Collection<Set<String>> setset ) {
        Set<String> result = new HashSet<String>();

        for( Set<String> set : setset ) {
            result.addAll( set );
        }

        return result;
    }


    private static Set<String> usualset( Set<Key> set ) {
        Set<String> result = new HashSet<String>();

        for( Key k : set ) {
            result.add( k.get_keyname() );
        }

        return result;
    }
}
