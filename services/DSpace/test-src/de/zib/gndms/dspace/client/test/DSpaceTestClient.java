package de.zib.gndms.dspace.client.test;

import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.model.dspace.StorageSize;

import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.08.2008, Time: 14:07:56
 */
public class DSpaceTestClient {

    public static void usage( ) {
        System.out.println( DSpaceTestClient.class.getName() + "-url <service url> -sn <scope-name> -ln <local-name>");
    }

    public static void main( String[] args ) {
        System.out.println("Running the C3Gird DSpace test client.");
		try{
            String url = null, sn = null, ln = null;
            if( args.length > 2 || args[0].equals( "-h" ) ){
                for( int i=0; i < args.length; ++i ) {
                    String arg = args[i];
                    if( arg.equals( "-url" ) ){
                        url = args[++i];
                    } else if( arg.equals( "-sn" ) ) {
                        sn = args[++i];
                    } else if( arg.equals( "-ln" ) ) {
                        ln = args[++i];
                    } else {
                        usage();
                        System.exit(1);
                    }
                }
            } else {
                usage();
                System.exit(1);
            }
            if( url == null || sn == null || ln == null ) {
                usage();
                System.exit(1);
            }

            DSpaceClient client = new DSpaceClient( url );
            SubspaceClient subclient = client.findSubspace( sn, ln );
            // todo insert useful uri
            String skuri = null;
            GregorianCalendar tt = new GregorianCalendar( );
            tt.add( Calendar.YEAR, 20 );
            StorageSize ssize = new StorageSize( );
            ssize.setAmount( 20 );
            ssize.setUnit( "TB" );
            SliceClient sc = subclient.createSlice( skuri, tt, ssize );
            System.out.println( "Check set values" );
            System.out.println( "slicekind: "+ skuri.equals( sc.getSliceKind() ) );
            System.out.println( "terminationtim: " + tt.equals( sc.getTerminationTime() ) );
            System.out.println( "storage size:" + ssize.equals( sc.getTotalStorageSize() ) );
        } catch ( ArrayIndexOutOfBoundsException e ) {
            usage();
            System.exit(1);
        } catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
    }
}
