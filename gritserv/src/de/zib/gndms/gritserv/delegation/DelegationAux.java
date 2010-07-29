package de.zib.gndms.gritserv.delegation;

import de.zib.gndms.gritserv.typecon.util.AxisTypeFromToXML;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import org.apache.axis.types.URI;
import org.globus.axis.util.Util;
import org.globus.delegation.DelegationUtil;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.impl.security.descriptor.ClientSecurityDescriptor;
import org.globus.wsrf.utils.AddressingUtils;
import org.apache.commons.codec.binary.Base64;
import org.oasis.wsrf.lifetime.ImmediateResourceTermination;
import org.oasis.wsrf.lifetime.WSResourceLifetimeServiceAddressingLocator;
import org.oasis.wsrf.lifetime.Destroy;
import org.xml.sax.InputSource;
import types.ContextT;
import types.ContextTEntry;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

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

    public static String defaultProxyFileName( String uid ) {
        return "/tmp/x509up_u" +uid;
    }

    public static String createDelationAddress( String addr ) throws URI.MalformedURIException {
        URI uri = new URI( addr );
        return uri.getScheme()  + "://" + uri.getHost( ) + ":" + uri.getPort() +  "/wsrf/services/DelegationFactoryService";
    }
    

    public static EndpointReferenceType createProxy( String uri, GlobusCredential credential ) throws Exception {

        // acquire cert chain
        ClientSecurityDescriptor desc = new ClientSecurityDescriptor();

       // desc.setGSITransport( (Integer) Constants.ENCRYPTION );
        org.ietf.jgss.GSSCredential gss = new org.globus.gsi.gssapi.GlobusGSSCredentialImpl( credential,
            org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT);
        desc.setGSSCredential( gss );

        desc.setGSITransport( (Integer) Constants.SIGNATURE );
        Util.registerTransport();
        desc.setAuthz( NoAuthorization.getInstance() );

        System.out.println( "connecting to service: " + uri );
        EndpointReferenceType delegEpr = AddressingUtils.createEndpointReference( uri, null);
        System.out.println( "epr: " + delegEpr );

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
                final String uuepr = e.get_value().toString( );
                System.out.println( "uuepr: " + uuepr );

                final Base64 b64   = new Base64(4000, new byte[] { }, true);
                byte[] ba = b64.decode(uuepr);
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

        return epr;
    }


    public static GlobusCredential fromByteArray( byte[] ba  ){

        ByteArrayInputStream bo = new ByteArrayInputStream( ba );
        try {
            return new GlobusCredential( bo );
        } catch ( GlobusCredentialException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException( e );
        }
    }


    public static byte[] toByteArray( GlobusCredential cred  ){

        ByteArrayOutputStream bo = new ByteArrayOutputStream( );
        try {
            cred.save( bo );
            return bo.toByteArray();
        } catch ( IOException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException( e );
        }
    }


    public static void addDelegationEPR( ContextT con, EndpointReferenceType epr ) throws IOException, SerializationException {

        StringWriter sw = new StringWriter( );
        AxisTypeFromToXML.toXML( sw, epr );

        ContextTEntry[] entries = con.getEntry();
        ArrayList<ContextTEntry> al;
        if ( entries != null ) {
            al = new ArrayList<ContextTEntry>( entries.length + 1 );
            for( ContextTEntry e : entries )
                al.add( e );
        } else
            al = new ArrayList<ContextTEntry>( 1 );


        ContextTEntry ct = new ContextTEntry( );

        ByteArrayOutputStream bse = new ByteArrayOutputStream( );
        ObjectOutputStream oos = new ObjectOutputStream( bse );
        oos.writeObject( ObjectSerializer.toString( epr , QNAME ) );

        final Base64 b64   = new Base64(4000, new byte[] { }, true);
        final String uuepr = b64.encodeToString(bse.toByteArray());
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


    public static void destroyDelegationEPR ( EndpointReferenceType epr ) throws ServiceException, RemoteException {

        WSResourceLifetimeServiceAddressingLocator ll = new WSResourceLifetimeServiceAddressingLocator();

        ImmediateResourceTermination term = ll.getImmediateResourceTerminationPort( epr );
        term.destroy( new Destroy() );
    }
}
