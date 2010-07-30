package de.zib.gndmc.DSpace.tests;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;

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
            String skuri = "http://www.c3grid.de/G2/SliceKind/DMS";
            GregorianCalendar tt = new GregorianCalendar( );
            tt.add( Calendar.YEAR, 20 );
            long ssize = (long) (20 * 1024 * Math.pow( 10, 12  ));
            SliceClient sc = subclient.createSlice( skuri, tt, ssize );
            System.out.println( "Check set values" );
            System.out.println( "slicekind: "+ skuri.equals( sc.getSliceKind() ) );
            System.out.println( "terminationtime: " + tt.equals( sc.getTerminationTime() ) );
            System.out.println( "termination time: " + sc.getTerminationTime() );
            System.out.println( "termination time expected: " + tt );
            System.out.println( "storage size: " +( ssize == sc.getTotalStorageSize() ) );
        } catch ( ArrayIndexOutOfBoundsException e ) {
            usage();
            System.exit(1);
        } catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
    }
}
