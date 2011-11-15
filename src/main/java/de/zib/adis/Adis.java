package de.zib.adis;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import de.zib.adis.abi.ABIi;

import de.zib.vold.common.Key;
import de.zib.vold.client.RESTClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Adis extends ABIi
{
        protected final Logger log = LoggerFactory.getLogger( this.getClass() );

        private RESTClient voldi;
        private String grid;

        public Adis( )
        {
                this.voldi = new RESTClient();
                this.grid = "c3grid";
        }

        public void setVoldURL( String voldURL )
        {
                voldi.setBaseURL( voldURL );
        }

        public void setEnc( String enc )
        {
                voldi.setEnc( enc );
        }

        public void setGrid( String grid )
        {
                this.grid = grid;
        }

        public void checkState( )
        {
                voldi.checkState();

                if( null == grid )
                {
                        throw new IllegalStateException( "Tried to operate on ADiS while it had not been initialized yet. Set proper gridname before!" );
                }
        }


        public String getRole( String role )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, role, "..." ) );

                if( null == _result )
                {
                        return null;
                }

                // should be exactly one entry!
                if( 1 < _result.size() )
                {
                        log.warn( "More than one " + role + " endpoint registered!" );
                }
                for( Map.Entry< Key, Set< String > > entry: _result.entrySet() )
                {
                        if( 0 == entry.getValue().size() )
                        {
                                return null;
                        }
                        if( 1 < entry.getValue().size() )
                        {
                                log.warn( "More than one " + role + " endpoint registered!" );
                        }

                        return entry.getValue().iterator().next();
                }

                return null;
        }

        public String getDMS( )
        {
                return getRole( "DMS" );
        }

        public String getWSS( )
        {
                return getRole( "WSS" );
        }

        public Collection< String > listOAIs( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "oai", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                return mergedValues( _result );
        }

        public Collection< String > listImportSites( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "import", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                return flatten( _result.values() );
        }

        public Map< String, String > listExportSites( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "export", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                return flatmap( _result );
        }

        public Collection< String > listWorkflows( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "workflow", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                return usualset( _result.keySet() );
        }

        public Collection< String > listGORFXbyOID( String oidprefix )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "oidprefix", oidprefix ) );

                if( null == _result )
                {
                        return null;
                }

                return mergedValues( _result );
        }

        public Map< String, String > listPublisher( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "publisher", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                return flatmap( _result );
        }

        public Map< String, String > getEPbyWorkflow( String workflow )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "workflow", workflow ) );

                if( null == _result )
                {
                        return null;
                }

                Map< String, String > result = new HashMap< String, String >();

                // should be exactly one entry
                for( Map.Entry< Key, Set< String > > entry: _result.entrySet() )
                {
                        for( String gram: entry.getValue() )
                        {
                                Map< Key, Set< String > > _gramres = voldi.lookup( new Key( grid, "gram", gram ) );

                                if( null == _gramres )
                                {
                                        log.warn( "Workflow " + workflow + " had a GRAM EndPoint " + gram + " registered, which has neither a GridFTP nor a SubSpace endpoint." );
                                        continue;
                                }

                                result.putAll( flatmap( _gramres ) );
                        }
                }

                return result;
        }



        public boolean setRole( String role, String endpoint )
        {
                // guard
                {
                        checkState();
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, role, "" ), endpoint ) );

                return 0 == _result.size();
        }

        public boolean setDMS( String endpoint )
        {
                return setRole( "DMS", endpoint );
        }

        public boolean setWSS( String endpoint )
        {
                return setRole( "WSS", endpoint );
        }

        public boolean setExport( String name, String subspace )
        {
                // guard
                {
                        checkState();

                        if( null == name )
                        {
                                name = "";
                        }
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "export", name ), subspace ) );

                return 0 == _result.size();
        }

        public boolean setImport( String name, String subspace )
        {
                // guard
                {
                        checkState();

                        if( null == name )
                        {
                                name = "";
                        }
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "import", name ), subspace ) );

                return 0 == _result.size();
        }

        public boolean setWorkflows( String subspace, String gram, Collection< String > workflows )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > request = new HashMap< Key, Set< String > >();
                Set< String > gramset = simpleset( gram );

                // add workflow |--> gram
                for( String workflow: workflows )
                {
                        request.put( new Key( grid, "workflow", workflow ), gramset );
                }

                // add gram |--> subspace
                request.put( new Key( grid, "gram", gram ), simpleset( subspace ) );

                Map< String, String > _result = voldi.insert( null, request );

                return 0 == _result.size();
        }

        public boolean setOIDPrefixe( String gorfx, Collection< String > oidprefixe )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > request = new HashMap< Key, Set< String > >();

                // add prefix |--> gorfx
                for( String oidprefix: oidprefixe )
                {
                        request.put( new Key( grid, "oidprefix", oidprefix ), simpleset( gorfx ) );
                }

                Map< String, String > _result = voldi.insert( null, request );

                return 0 == _result.size();
        }

        public boolean setOAI( String endpoint )
        {
                // guard
                {
                        checkState();
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "oai", "" ), endpoint ) );

                return 0 == _result.size();
        }



        private Map< Key, Set< String > > simplemap( Key key, String value )
        {
                Map< Key, Set< String > > result = new HashMap< Key, Set< String > >();

                Set< String > set = new HashSet< String >();
                set.add( value );

                result.put( key, set );

                return result;
        }

        private Set< String > simpleset( String s )
        {
                Set< String > set = new HashSet< String >();

                set.add( s );

                return set;
        }

        private Collection< String > mergedValues( Map< Key, Set< String > > map )
        {
                // merge all Sets
                Set< String > result = new HashSet< String >();

                for( Set< String > set: map.values() )
                {
                        result.addAll( set );
                }

                return result;
        }

        private Map< String, String > flatmap( Map< Key, Set< String > > map )
        {
                Map< String, String > result = new HashMap< String, String >();

                for( Map.Entry< Key, Set< String > > entry: map.entrySet() )
                {
                        if( 1 != entry.getValue().size() )
                        {
                                log.warn( "Unexpected state in database: got " + entry.getValue().size() + " values for " + entry.getKey().toString() + "." );
                                continue;
                        }

                        result.put( entry.getKey().get_keyname(), entry.getValue().iterator().next() );
                }

                return result;
        }

        private Set< String > flatten( Collection< Set< String > > setset )
        {
                Set< String > result = new HashSet< String >();

                for( Set< String > set: setset )
                {
                        result.addAll( set );
                }

                return result;
        }

        private Set< String > usualset( Set< Key > set )
        {
                Set< String > result = new HashSet< String >();

                for( Key k: set )
                {
                        result.add( k.get_keyname() );
                }

                return result;
        }
}
