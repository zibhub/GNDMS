package de.zib.gndmc;

import de.zib.gndmc.GORFX.GORFXTestClientUtils;
import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDTypeWriter;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.gsi.GlobusCredentialException;
import types.ContextT;
import types.FileTransferResultT;

import javax.xml.namespace.QName;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 22.07.2010, Time: 17:05:28
 */
public class SliceInOutClient {


    public void run( ) throws Exception, RemoteException, GlobusCredentialException {

        String sourcePath ="";
        boolean useDelegation = true;
        int uid = 100;
        String gorfxURI = "";


        SliceInOutClient client = new SliceInOutClient( /* propsfile */ );
        DSpaceClient dc = client.getDSpaceClient();
        String v = client.queryDMSVersion();
        System.out.println( "Connected to GNDMS: " + v );

        System.out.println( "Creating slice" );
        SliceClient sc = client.createSlice();

        String loc = sc.getSliceLocation();


        ContextT ctx = new ContextT( );
        EndpointReferenceType delegatEPR = null;
        if( useDelegation ) {
            System.out.println( "Setting up delegation" );
            delegatEPR = GORFXTestClientUtils.setupDelegation( ctx, dspaceURI, uid );
        }
        performCopy( gorfxURI, ctx, sourcePath, loc );


        System.out.println( "\nNow the otherway round!" );
        performCopy( gorfxURI, ctx, sourcePath, loc );


        System.out.println( "\nOkay, all done. Cleaning up!" );
        System.out.println( "* Destroying Slice" );
        client.destroySlice( sc );

        if( useDelegation ) {
            System.out.println( "* Destroying Slice" );
            DelegationAux.destroyDelegationEPR( delegatEPR );
        }
        System.out.println( "Done." );
    }

    
    public static void performCopy( String gorfxURI, ContextT ctx, String source, String dest ) throws Exception {

        FileTransferORQ orq = new FileTransferORQ();
        orq.setSourceURI( source );
        orq.setTargetURI( dest );
        System.out.println( "Copy " + source + "->" + dest );
        FileTransferResultT res =
            GORFXTestClientUtils.commonTaskExecution( "transfer", gorfxURI,
                FileTransferORQXSDTypeWriter.write( orq ), ctx, null, 1000, FileTransferResultT.class );

        System.out.println( "CheckIn complete: "  );
        showCopyResult( res );

    }


    public String sliceKindURI;
    public String subSpaceQName;
    public SubspaceClient subSpaceClient;
    private DSpaceClient dspaceClient;
    private String dspaceURI;

    public SliceInOutClient( ) {

    }

    public SliceInOutClient( Properties prop ) {
        this();
        setProperties( prop );
    }


    public SliceInOutClient( String propFileName ) {
        this();
        setProperties( propFileName );
    }

    private void setProperties( Properties prop ) {
    }


    public SliceClient createSlice( ) throws URI.MalformedURIException, RemoteException {

        Calendar tt = new GregorianCalendar( );
        tt.add( Calendar.YEAR, 20 );
        long ssize = (long) (20 * 1024 * Math.pow( 10, 3 ));
        return createSlice( tt, ssize );
    }


    public SliceClient createSlice( Calendar tt, long ssize ) throws URI.MalformedURIException, RemoteException {

        return subSpaceClient.createSlice( sliceKindURI, tt, ssize );
    }


    public SubspaceClient getSubSpaceClient() throws URI.MalformedURIException, RemoteException {

        if( subSpaceClient == null  ) {
            if( subSpaceQName == null || subSpaceQName.trim().equals( "" ) )
                throw new IllegalStateException( "SubSpace URI is required" );

            subSpaceClient =  getDSpaceClient().findSubspace( new QName( subSpaceQName ) );

        }
        return subSpaceClient;
    }


    public DSpaceClient getDSpaceClient() throws URI.MalformedURIException, RemoteException {

        if( dspaceURI == null  ) {
            if( dspaceURI == null || dspaceURI.trim().equals( "" ) )
                throw new IllegalStateException( "dspace URI is required" );

            dspaceClient = new DSpaceClient( dspaceURI );

        }
        
        return dspaceClient;
    }


    public String queryDMSVersion ( ) throws URI.MalformedURIException, RemoteException {
        return (String) getDSpaceClient().callMaintenanceAction( ".sys.ReadGNDMSVersion", null );
    }

    public void destroySlice( SliceClient sl ) throws RemoteException {
        sl.setTerminationTime( new GregorianCalendar( ) );
    }


    // timeout in seconds
    public boolean waitForSliceDestruction( SliceClient sl, int timeout, boolean verbose )  {

        boolean dest=false;
        if( verbose ) System.out.println( "Waiting for slice removal (this may take a while): " );
        String rs;
        for( int i=0; i < timeout && !dest; ++i ) {
            if( verbose ) System.out.print( i+1 + " " );
            try {
                rs = sl.getSliceKind();
                if( rs == null || "".equals( rs.trim() ) ) {
                    if( verbose ) System.out.println( "rs is empty..." );
                    dest=true;
                }
                try {
                    Thread.sleep( 1000 );
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            } catch ( RemoteException e ) {
                dest=true;
                if( verbose ) System.out.println( "\nSource slice removed" );
            }
        }
        return dest;
    }

    
    public void setProperties( String fn ) {

        // read property file
        InputStream f = null;
        RuntimeException exc = null;
        try {
            f = new FileInputStream( fn );
            Properties prop = new Properties( );
            prop.load( f );
            setProperties( prop );
        } catch ( FileNotFoundException e ) {
            exc =  new RuntimeException( "Failed to load properties file " + fn, e );
        } catch ( IOException e ) {
            exc =  new RuntimeException( "Failed to read properties from file " + fn, e );
        } finally {
            if( f != null )
                try {
                    f.close( );
                } catch ( IOException e ) {
                    RuntimeException re =
                        new RuntimeException( "Failed to close properties file " + fn, e );
                    if( exc != null )
                        re.initCause( exc );
                    throw re;
                }
            if( exc != null )
                throw exc;
        }
    }


    public static void showCopyResult( FileTransferResultT res ) throws Exception {

        MessageElement[] mes = res.get_any();
        StringWriter sw = new StringWriter( );
        for( MessageElement me : mes )
            sw.write( "    " + ( (String) me.getObjectValue( String.class ) ) + '\n' ) ;

        System.out.println( "Copied the following file(s):\n" + sw.toString() );
    }
}
