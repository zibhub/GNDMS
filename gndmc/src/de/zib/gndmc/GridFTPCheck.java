package de.zib.gndmc;

import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.util.GlobusCredentialProviderImpl;
import de.zib.gndms.gritserv.util.LogAux;
import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.stuff.threading.FillOnce;
import org.globus.ftp.FeatureList;
import org.globus.ftp.FileInfo;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.Vector;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 29.07.2010, Time: 12:48:59
 */
public class GridFTPCheck extends AbstractApplication {

    @Option( name="-d", required=true, usage="dir to list"  )
    String dir;

    @Option( name="-h", required=false, usage="hostname" )
    String host="csr-pc35";

    @Option( name="-t", required=false, usage="Create Client in diffrent thread")
    boolean threaded = false;

    @Option( name="-u", required=true, usage="numeric uid" )
    String uid;

    final FillOnce<GridFTPClient> clientFlowVar = new FillOnce<GridFTPClient>(false);
    GridFTPClient c;


    public static void main( String[] args ) throws Exception {
        ( new GridFTPCheck() ).run( args );
        System.exit( 0 );
    }

    public void run( ) throws Exception {

        LogAux.stdSetupLogger( this.getClass() );
        final GlobusCredential cred = DelegationAux.findCredential(
            DelegationAux.defaultProxyFileName( uid ) );
        final CredentialProvider cp =
            new GlobusCredentialProviderImpl( GORFXConstantURIs.FILE_TRANSFER_URI, cred );

        if( threaded ) {
            System.out.println( "Threaded client creation" );
//            final NonblockingClientFactory nbc = new NonblockingClientFactory();
//            c = nbc.createClient( host, 2811, cp );
            java.util.concurrent.Executors.newFixedThreadPool(8).execute( new Runnable() {
                public void run() {
                    System.out.println( "08/15 client creation in executor" );
                    try {
                        final GridFTPClient c = new GridFTPClient(host, 2811);
                        cp.installCredentials( c );
                        clientFlowVar.set(c);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ServerException e) {
                        throw new RuntimeException(e);
                    }
                }
            });            
        } else {
            System.out.println( "08/15 client creation" );
            clientFlowVar.set(new GridFTPClient( host, 2811 ));
            cp.installCredentials( clientFlowVar.get() );
        }
        System.out.println( "Cliend created" );

  //      System.out.println( "Waiting" );
  //      for( int i = 0; i < 300; ++i )
  //          Thread.sleep( 1000 );
        

        c = clientFlowVar.get();
        // GridFTPClientCreator cc =
        //     new GridFTPClientCreator( "csr-pc35",  2811, cp );
        //c = cc.call();

        System.err.println( "requesting features: " );
        final FeatureList l = c.getFeatureList();

        System.err.println( "chdir to " + dir );
        c.changeDir( dir );
        //Vector v = c.mlsd();
        //for( Object o: v ) {
        //    System.out.println( (String) o );
        //}
        list();
    }

    
    private void list( ) throws Exception {
        System.err.println( "DONE. Requesing listing" );

        // TreeMap<String,String> files = new TreeMap<String,String>( );
        Vector<FileInfo> inf = c.list( );
        for( FileInfo fi: inf ) {
            if( fi.isFile() ) {
                String n = fi.getName();
                System.out.println( n );
         //       files.put( , null );
            }
        }
    }

}
