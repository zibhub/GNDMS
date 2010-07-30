package de.zib.gndms.gritserv.typecon;

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



import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.URI;
import types.*;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.08.2008, Time: 13:50:52
 *
 * If one introduces new ORQT types or changes an element type of an ORQT to an type which isn't listed in the
 * TypeMap. an new entry must be added to that map, consisting of the QName of that type an a matching class.
 */
public class GORFXClientTools {

    private static ConcurrentHashMap<String, URI> UriTable = new ConcurrentHashMap<String, URI>( );
    private static final HashMap<QName, Class> TypeMap = new HashMap<QName, Class>( );

    static {
        TypeMap.put( new QName( "http://www.w3.org/2001/XMLSchema", "anyURI" ), URI.class );
        TypeMap.put( new QName( "http://gndms.zib.de/c3grid/types", "DataDescriptorT" ), DataDescriptorT.class );
        TypeMap.put( new QName( "http://www.w3.org/2001/XMLSchema", "normalizedString" ), NormalizedString.class );
        TypeMap.put( new QName( "http://gndms.zib.de/common/types", "FileMappingSeqT"), FileMappingSeqT.class );
        TypeMap.put( new QName( "http://gndms.zib.de/common/types", "SliceReferenceT"), SliceReferenceT.class );
        TypeMap.put( new QName( "http://lofis.gndms.zib.de/LOFIS/LofiSet/types", ">LofiSetReference"), LofiSetReference.class );
        TypeMap.put( new QName( "http://gndms.zib.de/c3grid/types", ">PinORQT>Target"), PinORQTTarget.class );
        TypeMap.put( new QName( "http://dspace.gndms.zib.de/DSpace/Slice/types", ">SliceReference"), SliceReference.class );
    }

    // helpers for provider stage in
    
    public static URI getProviderStageInURI( ) {

        return getURI( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
    }


    public static ProviderStageInORQT createEmptyProviderStageInORQT ( ) throws SOAPException, IllegalAccessException, InstantiationException {

        ProviderStageInORQT orq = new ProviderStageInORQT();
        orq.setOfferType( getProviderStageInURI() );

        orq.set_any( getMessageElementsForFieldDescs( ProviderStageInORQT.getTypeDesc().getFields() ) );

        return orq;
    }

    
    // helpers for slice stage in

    public static URI getSliceStageInURI ( ) {

        return getURI( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }


    public static SliceStageInORQT createEmptySliceStageInORQT ( ) throws SOAPException, IllegalAccessException, InstantiationException {

        SliceStageInORQT orq = new SliceStageInORQT();
        orq.setOfferType( getSliceStageInURI() );

        orq.set_any( getMessageElementsForFieldDescs( SliceStageInORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static URI getInterSliceTransferURI( ) {

        return getURI( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
    }
    

    public static InterSliceTransferORQT createEmptyInterSliceTransferORQT( ) throws SOAPException, IllegalAccessException, InstantiationException {

        InterSliceTransferORQT orq = new InterSliceTransferORQT();
        orq.setOfferType( getInterSliceTransferURI() );

        orq.set_any( getMessageElementsForFieldDescs( InterSliceTransferORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static InterSliceTransferORQT createInterSliceTransferORQT( SliceReference src, SliceReference dest ) throws SOAPException, IllegalAccessException, InstantiationException {

        InterSliceTransferORQT orq = createEmptyInterSliceTransferORQT();
        MessageElement[] mes = orq.get_any();
        mes[0].setObjectValue( src );
        mes[1].setObjectValue( dest );
        return orq;
    }

    
    public static URI getRePublishSliceURI( ) {

        return getURI( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
    }


    public static RePublishSliceORQT createEmptyRePublishSliceORQT( ) throws SOAPException, IllegalAccessException, InstantiationException {

        RePublishSliceORQT orq = new RePublishSliceORQT();
        orq.setOfferType( getRePublishSliceURI() );

        orq.set_any( getMessageElementsForFieldDescs( RePublishSliceORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static URI getFileTransferURI( ) {

        return getURI( GORFXConstantURIs.FILE_TRANSFER_URI );
    }
    

    public static FileTransferORQT createEmptyFileTransferORQT( ) throws SOAPException, IllegalAccessException, InstantiationException {

        FileTransferORQT orq = new FileTransferORQT();
        orq.setOfferType( getFileTransferURI() );

        orq.set_any( getMessageElementsForFieldDescs( FileTransferORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static URI getLofiSetStageInURI ( ) {

        return getURI( GORFXConstantURIs.LOFI_SET_STAGE_IN_URI );
    }


    public static LofiSetStageInORQT createEmptyLofiSetStageInORQT ( ) throws SOAPException, IllegalAccessException, InstantiationException {

        LofiSetStageInORQT orq = new LofiSetStageInORQT();
        orq.setOfferType( getLofiSetStageInURI() );

        orq.set_any( getMessageElementsForFieldDescs( LofiSetStageInORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static URI getPinURI ( ) {

        return getURI( GORFXConstantURIs.PIN_URI );
    }


    public static PinORQT createEmptyPinORQT ( ) throws SOAPException, IllegalAccessException, InstantiationException {

        PinORQT orq = new PinORQT();
        orq.setOfferType( getPinURI() );

        orq.set_any( getMessageElementsForFieldDescs( PinORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static URI getRePublishLofiSetURI( ) {

        return getURI( GORFXConstantURIs.RE_PUBLISH_LOFI_SET_URI );
    }


    public static RePublishLofiSetORQT createEmptyRePublishLofiSetORQT( ) throws SOAPException, IllegalAccessException, InstantiationException {

        RePublishLofiSetORQT orq = new RePublishLofiSetORQT();
        orq.setOfferType( getRePublishLofiSetURI() );

        orq.set_any( getMessageElementsForFieldDescs( RePublishLofiSetORQT.getTypeDesc().getFields() ) );

        return orq;
    }


    public static ProviderStageInResultT createProviderStageInResultT() throws SOAPException, IllegalAccessException, InstantiationException {

        ProviderStageInResultT psr = new ProviderStageInResultT( );
        psr.setOfferType( getProviderStageInURI() );

        psr.set_any( getMessageElementsForFieldDescs( ProviderStageInResultT.getTypeDesc().getFields() ) );

        return psr;
    }


    public static FileTransferResultT createFileTransferResultT() throws SOAPException, IllegalAccessException, InstantiationException {

        FileTransferResultT ftr = new FileTransferResultT();
        ftr.setOfferType( getFileTransferURI() );

        ftr.set_any( getMessageElementsForFieldDescs( FileTransferResultT.getTypeDesc().getFields() ) );

        return ftr;
    }


    public static InterSliceTransferResultT createInterSliceTransferResultT() throws SOAPException, IllegalAccessException, InstantiationException {

        InterSliceTransferResultT res = new InterSliceTransferResultT();
        res.setOfferType( getInterSliceTransferURI() );

        res.set_any( getMessageElementsForFieldDescs( InterSliceTransferResultT.getTypeDesc().getFields() ) );

        return res;
    }


    public static RePublishSliceResultT createRePublishSliceResultT() throws SOAPException, IllegalAccessException, InstantiationException {

        RePublishSliceResultT res = new RePublishSliceResultT();
        res.setOfferType( getRePublishSliceURI() );

        res.set_any( getMessageElementsForFieldDescs( RePublishSliceResultT.getTypeDesc().getFields() ) );

        return res;
    }


    public static SliceStageInResultT createSliceStageInResultT() throws SOAPException, IllegalAccessException, InstantiationException {

        SliceStageInResultT res = new SliceStageInResultT();
        res.setOfferType( getSliceStageInURI() );

        res.set_any( getMessageElementsForFieldDescs( SliceStageInResultT.getTypeDesc().getFields() ) );

        return res;
    }


    public static MessageElement[] getMessageElementsForFieldDescs( FieldDesc[] fds ) throws SOAPException, InstantiationException, IllegalAccessException {

        // prune offerType
        ArrayList<MessageElement> mes = new ArrayList( fds.length - 1 );
        for( FieldDesc fd: fds ) {

            if( fd instanceof ElementDesc ) {
                ElementDesc ed = (ElementDesc) fd;

                for( int i=0; i < ed.getMinOccurs(); ++i )
                    mes.add( getMessageElementForFieldDesc( ed ) );
            }
        }

        return mes.toArray( new MessageElement[0] );
    }


    public static MessageElement getMessageElementForFieldDesc( FieldDesc fd ) throws SOAPException, IllegalAccessException, InstantiationException {

        if( fd == null )
            throw new IllegalStateException( "null field desc received" );

        return createElementForField( fd, TypeMap.get( fd.getXmlType() ).newInstance() );
    }


    public static MessageElement createElementForField( FieldDesc fd, Object val ) throws SOAPException {

        if( val == null )
            throw new IllegalStateException( "no value received" );

        MessageElement me =  new MessageElement( fd.getXmlName( ) );
        me.setType( fd.getXmlType( ) );
        me.setObjectValue( val );

        return me;
    }


    private static URI getURI( String key ) {

        if(! UriTable.containsKey( key ) ) {
            try {
                UriTable.put( key, new URI( key ) );
            } catch (URI.MalformedURIException e) {
                // this cannot happen
                e.printStackTrace();
            }
        }
        return UriTable.get( key );
    }

    // just for debuging
    public static void showMES( MessageElement[] mes ) {

        if( mes != null ) {
            System.out.println( "mes.length: " + mes.length );
            for( int i=0; i < mes.length; ++i ) {
                MessageElement me = mes[i];
                if ( me != null ) {
                    System.out.println( i+ ". " + me.getName( )+": " + me.getType( ) );
                    System.out.println( "toString: " + me.toString() );
                } else
                    System.out.println( i+": me is null" );
            }
        } else
            System.out.println( "no elements received" );
    }
}
