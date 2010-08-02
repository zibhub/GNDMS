package de.zib.gndms.gritserv.typecon.types;

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



import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInResultWriter;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;
import types.ProviderStageInResultT;
import types.SliceReference;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 17:49:16
 */
public class ProviderStageInResultXSDTypeWriter extends AbstractXSDTypeWriter<ProviderStageInResultT>
    implements ProviderStageInResultWriter
{

    
    public void writeSliceReference( String srf ) {

        try {
            EndpointReferenceType epr = SliceIdToEPR( srf );
            SliceReference srt = ( SliceReference ) getProduct().get_any()[0].getObjectValue();
            srt.setEndpointReference( epr );
        } catch ( Exception e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IllegalStateException( e.getMessage(), e );
        }
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createProviderStageInResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        // Not required here
    }


    static public ProviderStageInResultT writeResult( @NotNull ProviderStageInResult prs ) {

        ProviderStageInResultXSDTypeWriter wrt = new ProviderStageInResultXSDTypeWriter();
        wrt.begin();
        wrt.writeSliceReference( prs.getSliceKey() );
        wrt.done( );
        return wrt.getProduct();
    }


    static public EndpointReferenceType SliceIdToEPR( String sid ) throws Exception {

        String s = ServiceHost.getBaseURL( ).toString( ) + "gndms/Slice";

        SimpleResourceKey sk =
            new SimpleResourceKey( new QName("http://dspace.gndms.zib.de/DSpace/Slice", "SliceKey"), sid );
        return AddressingUtils.createEndpointReference( s, sk );
    }
}
