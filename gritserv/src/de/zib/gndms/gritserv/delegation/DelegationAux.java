package de.zib.gndms.gritserv.delegation;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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


import de.zib.gndms.gritserv.typecon.util.AxisTypeFromToXML;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import org.apache.axis.types.URI;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
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
import org.oasis.wsrf.lifetime.Destroy;
import org.oasis.wsrf.lifetime.ImmediateResourceTermination;
import org.oasis.wsrf.lifetime.WSResourceLifetimeServiceAddressingLocator;
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
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 10.02.2009, Time: 15:44:09
 */
public class DelegationAux {

    public static final String DELEGATION_EPR_KEY = "DelegationEPR";
    public static final QName QNAME = new QName("", "DelegatedEPR");
    public static Logger logger = Logger.getLogger( DelegationAux.class ); 

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

        logger.info( "connecting to service: " + uri );
        EndpointReferenceType delegEpr = AddressingUtils.createEndpointReference( uri, null);
        logger.debug( "epr: " + delegEpr );

        X509Certificate[] certs = DelegationUtil.getCertificateChainRP( delegEpr, desc );

        if( certs == null  )
            throw new Exception( "No Certs received" );

        if( logger.isDebugEnabled() ) {
            int len = certs.length;
            logger.debug( "Cerfiicate chain length: " + len );
            if( len > 0 )
                logger.debug( "Chain head: " + certs[0] );
        }


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
                logger.debug( "encoded delegation epr: " + uuepr );

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
        logger.debug( "base64 encoded uepr: \"" + uuepr+ "\"" );
        ct.set_value( new NormalizedString( uuepr ) );
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


    public static void destroyDelegationEPR ( EndpointReferenceType epr ) throws ServiceException, RemoteException {

        WSResourceLifetimeServiceAddressingLocator ll = new WSResourceLifetimeServiceAddressingLocator();

        ImmediateResourceTermination term = ll.getImmediateResourceTerminationPort( epr );
        term.destroy( new Destroy() );
    }
}
