package de.zib.adis;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;


public class ABI
{
        @Parameter( names = "--usage", hidden=true )
        private boolean usage;

        @Parameter( names={ "--baseurl", "-b" }, description="Base URL of VolD database", required=true )
        private String voldURL;

        @Parameter( names = { "--grid", "-g" }, description="Grid name" )
        private String grid = "c3grid";

        @Parameter( names = { "--enc", "-e" }, description="Encoding" )
        private String enc = "utf-8";

        @Parameter( description="Commands" )
        private List< String > commands = new ArrayList< String>();

        public static void main( String[] args )
        {
                ABI abi = new ABI();

                JCommander jc = new JCommander( abi, args );

                if( abi.usage )
                {
                        jc.usage();
                        System.exit( 0 );
                }

                Adis adis = new Adis( );
                adis.setVoldURL( abi.voldURL );
                adis.setEnc( abi.enc );
                adis.setGrid( abi.grid );

                String resultstr = null;
                Collection< String > resultcol = null;
                Map< String, String > resultmap = null;

                if( 1 == abi.commands.size() )
                {
                        String cmd = abi.commands.get( 0 );

                        if( cmd.equals( "getdms" ) )
                                resultstr = adis.getDMS();
                        else if( cmd.equals( "getwss" ) )
                                resultstr = adis.getWSS();
                        else if( cmd.equals( "listoais" ) )
                                resultcol = adis.listOAIs();
                        else if( cmd.equals( "listimportsites" ) )
                                resultcol = adis.listImportSites( );
                        else if( cmd.equals( "listexportsites" ) )
                                resultmap = adis.listExportSites( );
                        else if( cmd.equals( "listworkflows" ) )
                                resultcol = adis.listWorkflows();
                        else if( cmd.equals( "listpublisher" ) )
                                resultmap = adis.listPublisher();
                        else
                                commands();
                }
                else if( 2 == abi.commands.size() )
                {
                        String cmd = abi.commands.get( 0 );
                        String par = abi.commands.get( 1 );

                        if( cmd.equals( "listgorfxbyoid" ) )
                                resultcol = adis.listGORFXbyOID( par );
                        else if( cmd.equals( "getepbyworkflow" ) )
                                resultmap = adis.getEPbyWorkflow( par );
                        else commands();
                }

                if( null != resultstr )
                {
                        System.out.println( resultstr );
                }
                else if( null != resultcol )
                {
                        System.out.println( resultcol );
                }
                else if( null != resultmap )
                {
                        System.out.println( resultmap );
                }
        }

        private static void commands( )
        {
                System.out.println( "valid commands are:" );
                System.out.println( "   getwss" );
                System.out.println( "   getdms" );
                System.out.println( "   listoais" );
                System.out.println( "   listimportsites" );
                System.out.println( "   listexportsites" );
                System.out.println( "   listworkfows" );
                System.out.println( "   listpublisher" );
                System.out.println( "   listgorfxbyoid OIDPREFIX" );
                System.out.println( "   getepbyworkflow WORKFLOW" );
        }
}
