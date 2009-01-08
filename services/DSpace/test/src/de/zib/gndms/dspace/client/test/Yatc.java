package de.zib.gndms.dspace.client.test;

import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.slice.stubs.types.SliceReference;
import de.zib.gndms.dspace.slice.common.SliceConstants;
import de.zib.gndms.dspace.common.DSpaceConstants;
import de.zib.gndms.model.dspace.SliceKind;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.rmi.RemoteException;

import org.apache.axis.types.URI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.ReferencePropertiesType;
import org.apache.axis.message.MessageElement;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.*;

import javax.xml.namespace.QName;


/**
 * Yet another test client
 *
 * This test client does unit testing on the dspace entities.
 * It expects a dspace which is set up using the default setup-dataprovider.sh script.
 * If there are some more subspaces or the like, some tests may trigger assertions even so they are correct.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.10.2008, Time: 17:39:54
 */
public class Yatc {

    private String dspaceURI;
    private static final String SCHEMA_URI = "http://www.c3grid.de/G2/Subspace";
    private String localName;
    private String scopeName;
    private String gsiPath;
    private DSpaceClient client;
    private SliceClient slice;
    // some slice test values required in different methods
    private GregorianCalendar tt;
    private long ssize;
    private String skuri;


    // Params should be self-explanatory. gsiPath denots the public grid ftp location
    // of the provided workspace. And scope- resp. localName are the address of the test
    // subspace.
    @Parameters( { "dspaceURI", "scopeName", "localName", "gsiPath" } )
    public Yatc( String dspaceURI, String scopeName, String localName, String gsiPath ) {
        this.dspaceURI = dspaceURI;
        this.scopeName = scopeName;
        this.localName = localName;
        this.gsiPath = gsiPath;
    }


    @BeforeClass( groups={ "dspace", "subspace" } )
    public void beforeTest( ) throws Exception {
        client = new DSpaceClient( dspaceURI );
    }



    @Parameters( { "noSubspaces" } )
    @Test( groups={ "dspace"  } ) 
    public void runDspaceTests( int noSubspaces ) throws Exception {

        System.out.println( ">>> Performing dspace tests" );

        System.out.println( "checking listSupportedSchemas()" );
        URI[] uris = client.listSupportedSchemas();

        assertEquals ( uris.length, 1, "length of result" );
        URI suri = new URI( SCHEMA_URI );
        assertEquals ( uris[0], suri, "expected value?" );

        System.out.println( "\nChecking listPublicSubspaces() and getSubspace( )" );
        SubspaceReference[] srefs = client.listPublicSubspaces( suri );
        assertEquals ( srefs.length, noSubspaces, "length of result"  );

        SubspaceClient sc = client.findSubspace( scopeName, localName ); // findSubspace uses getSubspace

        assertEquals ( srefs[0].getEndpointReference().toString(), sc.getEndpointReference( ).toString(), "equal subspace eprs?" );
    }


    @Test( dependsOnGroups={ "dspace" }, groups={"subspace"} )
    public void runSubspaceTests( ) throws Exception {

        System.out.println( "\n>>> Performing dspace.subspace tests" );
        SubspaceClient subc = client.findSubspace( scopeName, localName ); // findSubspace uses getSubspace

        System.out.println( "checking listCreatableSliceKinds()" );
        URI[] sks = subc.listCreatableSliceKinds();
        assertEquals ( sks.length, 2, "slice kind count" );
        URI sk1 = new URI(  "http://www.c3grid.de/G2/SliceKind/Staging" );
        assertEquals ( sks[0], sk1, "First slice kind uri" );
        URI sk2 = new URI(  "http://www.c3grid.de/G2/SliceKind/DMS" );
        assertEquals ( sks[1], sk2, "Second slice kind uri" );

        System.out.println( "\nChecking createSubspace()" );

        skuri = "http://www.c3grid.de/G2/SliceKind/DMS";
        tt = new GregorianCalendar( );
        tt.add( Calendar.YEAR, 20 );
        ssize = (long) (20 * 1024 * Math.pow( 10, 3 ));
        slice = subc.createSlice( skuri, tt, ssize );
        sliceTests( slice, gsiPath, scopeName, localName );
        analyseEPR( slice.getEndpointReference() );
    }


    @Parameters( { "testScopeName", "testLocalName", "testGsiPath" } )
    @Test( dependsOnGroups={ "subspace" }, groups={"slice"} )
    public void runSliceTests(  String testScopeName, String testLocalName, String testGsiPath ) throws Exception {

        System.out.println( "\n>>> Performing dspace.slice tests" );
        System.out.println( "checking transformSlice( SliceKind )" );

        skuri = "http://www.c3grid.de/G2/SliceKind/Staging";
        SliceReference sref = slice.transformSliceTo( skuri, null );
        SliceClient sc = new SliceClient( sref.getEndpointReference() );
        sliceTests( sc, gsiPath, scopeName, localName );


        boolean dest=false;
        System.out.println( "Waiting for slice removal: " );
        for( int i=0; i < 60 && !dest; ++i ) {
            System.out.print( i+1 + " " );
            try {
                slice.getSliceKind();
            } catch ( RemoteException e ) {
                dest=true;
                System.out.println( "\nSource slice removed" );
            }
            Thread.sleep( 1000 );
        }

        if( ! dest ) {
            System.out.println( "Transformed slice still exists." );
            assertTrue( dest, "slice destruction" );
       }

        System.out.println( "\n>>> Performing dspace.slice tests" );
        System.out.println( "checking transformSlice( Subspace )" );
        sref = sc.transformSliceTo( new QName( testScopeName, testLocalName ) , null );
        slice = new SliceClient( sref.getEndpointReference() );
        sliceTests( slice, testGsiPath, testScopeName, testLocalName );


        System.out.println( "checking transformSlice( SliceTypeSpecifierSubspace )" );
        skuri = "http://www.c3grid.de/G2/SliceKind/DMS";
        sref = slice.transformSliceTo( skuri, new QName( scopeName, localName ), null );
        sc = new SliceClient( sref.getEndpointReference() );
        sliceTests( sc, gsiPath, scopeName, localName );
    }


    private void sliceTests( SliceClient sc, String gp, String sn, String ln ) throws Exception {
        System.out.println( "Check slice values" );
        assertEquals(  skuri, sc.getSliceKind( ), "slicekind" );
        assertEquals(  tt, sc.getTerminationTime(), "terminationtime" );
        assertEquals(  ssize, sc.getTotalStorageSize(), "storage size" );
        String loc = sc.getSliceLocation();
        assertTrue(  loc.startsWith( gp ), "partial location check" );
        SubspaceClient ssref = sc.getSubspace();
        SubspaceClient subc = client.findSubspace( sn, ln );
        assertEquals(  ssref.getEndpointReference().toString( ),
            subc.getEndpointReference().toString( ),
            "Subspace epr"
        );

    }

    private void analyseEPR( EndpointReferenceType epr ) throws Exception {

        ReferencePropertiesType ept = epr.getProperties();
        MessageElement me = ept.get( SliceConstants.RESOURCE_KEY );
        String s = ( String ) me.getObjectValue( String.class );
        System.out.println( s );
    }
}
