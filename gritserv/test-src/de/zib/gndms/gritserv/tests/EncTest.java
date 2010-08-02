package de.zib.gndms.gritserv.tests;

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



import de.zib.gndms.gritserv.delegation.DelegationAux;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;
import types.ContextT;
import types.ContextTEntry;
import org.apache.commons.codec.binary.Base64;

import javax.xml.namespace.QName;
import java.io.*;

/**
 * @author: try ma ik jo rr a zib
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

				Base64 b64 = new Base64(0, new byte[] { }, true);
        String uuepr = b64.encodeToString( bse.toByteArray() );
        System.out.println( "uuepr: \"" + uuepr+ "\"" );

        byte[] bt = b64.decode(uuepr.getBytes());

        ByteArrayInputStream bis = new ByteArrayInputStream( bt );
        ObjectInputStream ois = new ObjectInputStream( bis );
        String eprs  = (String) ois.readObject();
        System.out.println( eprs );
    }
}
