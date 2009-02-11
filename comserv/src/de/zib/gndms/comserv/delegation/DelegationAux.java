package de.zib.gndms.comserv.delegation;

import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.security.descriptor.ClientSecurityDescriptor;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.message.addressing.EndpointReferenceType;
import types.ContextT;
import types.ContextTEntry;
import de.zib.gndms.typecon.util.AxisTypeFromToXML;

import java.io.*;
import java.util.ArrayList;

import org.globus.wsrf.impl.security.descriptor.ClientSecurityDescriptor;
import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.utils.AddressingUtils;

import org.globus.axis.util.Util;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import org.apache.axis.types.URI;

import org.globus.delegation.DelegationUtil;
import org.globus.delegation.DelegationConstants;


import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;

import org.globus.wsrf.impl.security.util.AuthUtil;

import java.security.cert.X509Certificate;

import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;

import sun.misc.UUEncoder;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 10.02.2009, Time: 15:44:09
 */
public class DelegationAux {

    public static final String DELEGATION_EPR_KEY = "DelegationEPR";
    public static final QName QNAME = new QName("", "DelegatedEPR");

    private DelegationAux() {
    }

    public static String createDelationAddress( String addr ) throws URI.MalformedURIException {
        URI uri = new URI( addr );
        return uri.getScheme()  + "://" + uri.getHost( ) + ":" + uri.getPort() +  "/wsrf/services/DelegationFactoryService";
    }

    public static EndpointReferenceType createProxy( String uri, GlobusCredential credential ) throws Exception {

        // aquire cert chain
        ClientSecurityDescriptor desc = new ClientSecurityDescriptor();

//        desc.setGSITransport( (Integer) Constants.ENCRYPTION );

        System.out.println( "connecting to service: " + uri );
        EndpointReferenceType delegEpr = AddressingUtils.createEndpointReference( uri, null);
        System.out.println( "epr: " + delegEpr );

        desc.setGSITransport( (Integer) Constants.SIGNATURE );
        Util.registerTransport();
        desc.setAuthz( NoAuthorization.getInstance() );

        X509Certificate[] certs = DelegationUtil.getCertificateChainRP( delegEpr, desc );

        if( certs == null  )
            throw new Exception( "No Certs received" );

        int len = certs.length;
        System.out.println( "Cert cnt: " + len );
        if( len > 0 )
            System.out.println( certs[0] );


        // get delegate:
        int tt = 600;
        return  DelegationUtil.delegate( uri, credential, certs[0], tt, true, desc);

    }


    public static EndpointReferenceType extractDelegationEPR( ContextT con ) throws Exception {

        ContextTEntry[] entries = con.getEntry();
        ArrayList<ContextTEntry> al = new ArrayList<ContextTEntry>( entries.length );
        EndpointReferenceType epr = null;

        for( ContextTEntry e : entries ) {
            if( e.getKey().equals( DELEGATION_EPR_KEY ) ) {
                //epr = eprFormXML( e.get_value().toString( ) );
                MyUUDecoder dec = new MyUUDecoder( );

                String uuepr =  deinlineUUString( e.get_value().toString( ) );
                System.out.println( "uuepr: " + uuepr );
                byte[] ba = dec.decodeBuffer( uuepr );
                ByteArrayInputStream bis = new ByteArrayInputStream( ba );
                ObjectInputStream ois = new ObjectInputStream( bis );
                String eprs  = (String) ois.readObject();
                InputSource is = new InputSource( new StringReader( eprs ) );

                epr = (EndpointReferenceType) ObjectDeserializer.deserialize( is , EndpointReferenceType.class);
            }  else
                al.add( e );
        }

        ContextTEntry[] r = al.toArray( new ContextTEntry[al.size()] );

        con.setEntry( r );

        UUEncoder enc;
        return epr;
    }


    public static void addDelegationEPR( ContextT con, EndpointReferenceType epr ) throws IOException, SerializationException {

        StringWriter sw = new StringWriter( );
        AxisTypeFromToXML.toXML( sw, epr );

        ContextTEntry[] entries = con.getEntry();
        ArrayList<ContextTEntry> al = new ArrayList<ContextTEntry>( entries.length + 1 );
        for( ContextTEntry e : entries )
                al.add( e );

        ContextTEntry ct = new ContextTEntry( );

        ByteArrayOutputStream bse = new ByteArrayOutputStream( );
        ObjectOutputStream oos = new ObjectOutputStream( bse );
        oos.writeObject( ObjectSerializer.toString( epr , QNAME ) );
        UUEncoder enc = new UUEncoder( );
        String uuepr = enc.encode( bse.toByteArray() );
        System.out.println( "uuepr: \"" + uuepr+ "\"" );
        ct.set_value( new NormalizedString( inlineUUString( uuepr ) ) );
        ct.setKey( new Token( DELEGATION_EPR_KEY ) );
        al.add( ct );

        con.setEntry( al.toArray( new ContextTEntry[al.size()] ) );
    }


    public static EndpointReferenceType eprFormXML( String xml ) throws Exception {

        InputStream is = new ByteArrayInputStream( xml.getBytes() );

        return AxisTypeFromToXML.fromXML( is, EndpointReferenceType.class );
    }

    public static GlobusCredential  findCredential( String fn ) throws GlobusCredentialException {
        if ( fn == null ) {
            return GlobusCredential.getDefaultCredential();
        } else {
            return new GlobusCredential( fn );
        }
    }


    public static String inlineUUString( String s ) {
        // remove all newlines
        return s.replace( "\n", "" );
    }


    // this uses the fact that in a uuencoded string every single data line is 60 chars wide.
    public static String deinlineUUString( String s ) {

        final String start = "encoder.buf";
        final String end = "end";
        StringWriter res = new StringWriter( );
        int idx = s.indexOf( start );
        idx += start.length( );
        res.write( s.substring( 0, idx ) + "\n" );

        // 61 cause nidx is excluded
        for( int nidx = idx + 61; nidx < s.length() - end.length(); idx = nidx, nidx += 61 )
            res.write( s.substring( idx, nidx ) + "\n" );

        // treat last line
        int lidx = s.lastIndexOf( end );

        res.write( s.substring( idx, lidx ) + "\n" );
        res.write( s.substring( lidx, s.length() ) );

        System.out.println( "deinlined:\n\"" + res.toString() + "\"" );
        return res.toString();
    }
}
