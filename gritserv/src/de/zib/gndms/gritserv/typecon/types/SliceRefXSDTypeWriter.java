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



import de.zib.gndms.model.gorfx.types.io.SliceRefWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefConverter;
import de.zib.gndms.model.dspace.types.SliceRef;
import types.SliceReference;

import javax.xml.namespace.QName;

import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 14:04:02
 */
public class SliceRefXSDTypeWriter extends AbstractXSDTypeWriter<SliceReference> implements SliceRefWriter {

    // use write() for one shot writing s.b.

    public void writeSliceRef( SliceRef sr ) {

        try {
            EndpointReferenceType epr = getEPRforSliceRef( sr );
            getProduct().setEndpointReference( epr );
        } catch ( Exception e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IllegalStateException( e.getMessage(), e );
        }
    }


    public void writeSliceGridSite( String site ) {
        //nothing to do
    }


    public void writeSliceId( String id ) {
        //nothing to do
    }


    public void writeSliceResourceName( QName name ) {
        //nothing to do
    }


    public void begin() {
        //nothing to do
        setProduct( new SliceReference( ) );
    }


    public void done() {
        //nothing to do
    }


    public static SliceReference write( SliceRef sr ) {

        SliceRefXSDTypeWriter wrt = new SliceRefXSDTypeWriter();
        SliceRefConverter conv = new SliceRefConverter( wrt, sr );
        conv.convert( );
        return wrt.getProduct();
    }


    public static EndpointReferenceType getEPRforSliceRef( SliceRef sr ) throws Exception {

        SimpleResourceKey sk = new SimpleResourceKey(  sr.getResourceKeyName(), sr.getResourceKeyValue() );
        return AddressingUtils.createEndpointReference( sr.getGridSiteId(), sk );
    }
}
