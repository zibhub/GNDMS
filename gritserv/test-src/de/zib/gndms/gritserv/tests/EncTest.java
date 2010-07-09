package de.zib.gndms.gritserv.tests;

import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.gritserv.delegation.MyUUDecoder;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;
import sun.misc.UUEncoder;
import types.ContextT;
import types.ContextTEntry;

import javax.xml.namespace.QName;
import java.io.*;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 11.02.2009, Time: 11:04:27
 */
public class EncTest {

    public static final QName QNAME = new QName("", "DelegatedEPR");

    public static void main ( String[] args ) throws Exception {

//        String delfac = "https://130.73.72.106:8443/wsrf/services/DelegationFactoryService";

 //       GlobusCredential cred = DelegationAux.findCredential( null );
 //       EndpointReferenceType epr = DelegationAux.createProxy( delfac, cred );

        String eprs =
        "<DelegatedEPR xsi:type=\"ns1:EndpointReferenceType\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns1=\"http://schemas.xmlsoap.org/ws/2004/03/addressing\">"
        +"<ns1:Address xsi:type=\"ns1:AttributedURI\">https://130.73.72.106:8443/wsrf/services/DelegationService</ns1:Address>"
        +"<ns1:ReferenceProperties xsi:type=\"ns1:ReferencePropertiesType\">"
        +"<ns1:DelegationKey xmlns:ns1=\"http://www.globus.org/08/2004/delegationService\">e54cb3e0-f834-11dd-93ce-a15e7f2269a5</ns1:DelegationKey>"
        +"</ns1:ReferenceProperties>"
        +"<ns1:ReferenceParameters xsi:type=\"ns1:ReferenceParametersType\"/>"
        +"</DelegatedEPR>";

        InputSource is = new InputSource( new StringReader( eprs ) );
        EndpointReferenceType epr = (EndpointReferenceType) ObjectDeserializer.deserialize( is , EndpointReferenceType.class);
        fieldTest( epr );

    }

    public static void fieldTest( EndpointReferenceType epr  ) throws Exception{

        ContextT ctx = new ContextT();
        ctx.setEntry( new ContextTEntry[]{} );
        DelegationAux.addDelegationEPR( ctx, epr );

        DelegationAux.extractDelegationEPR( ctx );
    }


    public static void dryRun( EndpointReferenceType epr ) throws Exception{

        ByteArrayOutputStream bse = new ByteArrayOutputStream( );
        ObjectOutputStream oos = new ObjectOutputStream( bse );
        String sepr =  ObjectSerializer.toString( epr , QNAME );
        System.out.println( sepr );
        oos.writeObject( sepr );
        UUEncoder enc = new UUEncoder( );
        String uuepr = enc.encode( bse.toByteArray() );
        System.out.println( "uuepr: \"" + uuepr+ "\"" );

        MyUUDecoder dec = new MyUUDecoder();
        byte[] bt = dec.decodeBuffer( uuepr );

        ByteArrayInputStream bis = new ByteArrayInputStream( bt );
        ObjectInputStream ois = new ObjectInputStream( bis );
        String eprs  = (String) ois.readObject();
        System.out.println( eprs );
    }
}
