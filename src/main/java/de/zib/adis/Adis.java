package de.zib.adis;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import de.zib.vold.client.RESTClient;

public class Adis
{
        private RESTClient voldi;
        private String grid;

        public Adis( )
        {
                this.voldi = new RESTClient();
                this.grid = "c3grid"
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

                return usualmap( _result );
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

                return flatten( _result.valueSet() );
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

                // should be exactly one entry
                for( Map.Entry< Key, Set< String > > entry: _result )
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

                return usualset( _result.keySet() );
        }

        public String getDMS( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "dms", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                // should be exactly one entry!
                if( 1 < _result.size() )
                {
                        log.warn( "More than one DMS endpoint registered!" );
                }
                for( Map.Entry< Key, Set< String > > entry: _result )
                {
                        if( 0 == entry.getValue().size() )
                        {
                                return null;
                        }
                        if( 1 < entry.getValue().size() )
                        {
                                log.warn( "More than one DMS endpoint registered!" );
                        }

                        return entry.getVale().iterator().next();
                }

                return null;
        }

        public String getWSS( )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > _result = voldi.lookup( new Key( grid, "wss", "..." ) );

                if( null == _result )
                {
                        return null;
                }

                // should be exactly one entry!
                if( 1 < _result.size() )
                {
                        log.warn( "More than one WSS endpoint registered!" );
                }
                for( Map.Entry< Key, Set< String > > entry: _result )
                {
                        if( 0 == entry.getValue().size() )
                        {
                                return null;
                        }
                        if( 1 < entry.getValue().size() )
                        {
                                log.warn( "More than one WSS endpoint registered!" );
                        }

                        return entry.getVale().iterator().next();
                }

                return null;
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

        public void setImport( String name, String subspace )
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

        public boolean setWorkflows( Collection< String > workflows, String gram, String subspace )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > request = new HashMap< Key, Set< String > >();

                // add workflow |--> gram
                for( String workflow: workflows )
                {
                        request.put( new Key( grid, "workflow", workflow ), gram );
                }

                // add gram |--> subspace
                request.put( new Key( grid, "gram", gram ), subspace );

                Map< String, String > _result = voldi.insert( null, request );

                return 0 == _result.size();
        }

        public void setOIDPrefix( Collection< String > oidprefixe, String gorfx )
        {
                // guard
                {
                        checkState();
                }

                Map< Key, Set< String > > request = new HashMap< Key, Set< String > >();

                // add prefix |--> gorfx
                for( String oidprefix: oidprefixe )
                {
                        request.put( new Key( grid, "oidprefix", oidprefix ), gorfx );
                }

                Map< String, String > _result = voldi.insert( null, request );

                return 0 == _result.size();
        }

        public void setOAI( String endpoint )
        {
                // guard
                {
                        checkState();
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "oai", "" ), endpoint ) );

                return 0 == _result.size();
        }

        public void setCentralDMS( String endpoint )
        {
                // guard
                {
                        checkState();
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "dms", "" ), endpoint ) );

                return 0 == _result.size();
        }

        public void setWSS( String endpoint )
        {
                // guard
                {
                        checkState();
                }

                Map< String, String > _result = voldi.insert( null, simplemap( new Key( grid, "wss", "" ), endpoint ) );

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

        private Collection< String > mergedValues( Map< Key, Set< String > > map )
        {
                // merge all Sets
                Set< String > result = new HashSet< String >();

                for( Set< String > set: map.valueSet() )
                {
                        result.addAll( set );
                }

                return result;
        }

        private Map< String, String > flatmap( Map< Key, Set< String > > map )
        {
                Map< String, String > result = new HashMap< String, String >();

                for( Map.Entry< Key, Set< String > > entry: map )
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

        private Map< String, String > usalmap( Map< Key, Set< String > > map )
        {
                Map< String, Set< String > > result = new HashMap< String, Set< String > >();

                for( Map.Entry< Key, Set< String > > entry: map )
                {
                        result.put( entry.getKey().get_keyname(), entry.getValue() );
                }

                return result;
        }

        private Set< String > flatten( Set< Set< String > > setset )
        {
                Set< String > result = new HashSet< String >();

                for( Set< String > set: setset )
                {
                        result.putAll( set );
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
