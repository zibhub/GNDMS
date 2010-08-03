package de.zib.gndms.gritserv.typecon.types;

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



import types.RePublishSliceORQT;
import de.zib.gndms.model.gorfx.types.io.RePublishSliceORQWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefConverter;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;

import javax.xml.soap.SOAPException;
import java.util.*;

import org.apache.axis.message.MessageElement;
import org.apache.axis.description.FieldDesc;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 05.11.2008, Time: 10:36:45
 */
public class RePublishSliceORQXSDTypeWriter extends AbstractXSDTypeWriter<RePublishSliceORQT>
    implements RePublishSliceORQWriter
{
    private SliceRefXSDTypeWriter sliceWriter;
    private SliceRefConverter sliceConverter;


    public RePublishSliceORQXSDTypeWriter( ) {
        sliceWriter = new SliceRefXSDTypeWriter();
        sliceConverter = new SliceRefConverter( );
        sliceConverter.setWriter( sliceWriter );
    }


    public void writeSourceSlice( SliceRef sr ) {
        
        sliceConverter.setModel( sr );
        sliceConverter.convert( );
        try {
            getProduct().get_any( )[0].setObjectValue( sliceWriter.getProduct() );
        } catch ( SOAPException e ) {
            throw new IllegalStateException( e.getMessage(), e );
        }
    }


    public void writeFileMap( TreeMap<String, String> fm ) {

        FieldDesc fd = RePublishSliceORQT.getTypeDesc().getFieldByName( "files" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            al.add(
                GORFXClientTools.createElementForField( fd, XSDReadWriteAux.write( fm ) ) );
        } catch ( SOAPException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );
    }


    public void writeJustEstimate( boolean je ) {
        // not required here
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // not required here
    }


    public void begin() {

        try {
            setProduct( GORFXClientTools.createEmptyRePublishSliceORQT() );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }


    public void done() {
        // not required here
    }


    public void writeId( String id ) {
        // Not required here
    }
}
