package de.zib.gndms.GORFX.common;

import org.apache.axis.types.URI;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.message.MessageElement;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.description.FieldDesc;
import types.*;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.08.2008, Time: 13:50:52
 */
public class GORFXClientTools {

    static ConcurrentHashMap<String, URI> UriTable = new ConcurrentHashMap<String, URI>( );

    // helpers for provider stage in
    
    public static URI getPoviderStageInURI( ) {

        return getURI( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
    }


    public static ProviderStageInORQT createEmptyProviderStageInORQT ( ) throws SOAPException {

        TypeDesc td = ProviderStageInORQT.getTypeDesc();
        ProviderStageInORQT orq = new ProviderStageInORQT();
        orq.setOfferType( getPoviderStageInURI() );

        ArrayList<MessageElement> mes = new ArrayList( 3 );

        FieldDesc[] fds = td.getFields();

        if( fds == null )
            throw new IllegalStateException( "no fields descrption" );

        // setup entry for stored data
        mes.add( createElementForField( td.getFieldByName( "stagedData" ), new DataDescriptorT( ) ) );
        // setup data file entry
        mes.add( createElementForField( td.getFieldByName( "dataFile" ), new NormalizedString( ) ) );
        // setup meta data file entry
        mes.add( createElementForField( td.getFieldByName( "metadataFile" ), new NormalizedString( ) ) );

        orq.set_any( mes.toArray( new MessageElement[0] ) );

        return orq;
    }

    // helpers for slice stage in

    public static URI getSliceStageInURI ( ) {

        return getURI( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }


    public static SliceStageInORQT createEmptySliceStageInORQT ( ) throws SOAPException {

        TypeDesc td = SliceStageInORQT.getTypeDesc();
        SliceStageInORQT orq = new SliceStageInORQT();
        orq.setOfferType( getPoviderStageInURI() );

        ArrayList<MessageElement> mes = new ArrayList( 4 );

        FieldDesc[] fds = td.getFields();

        if( fds == null )
            throw new IllegalStateException( "no fields descrption" );

        // setup grid site entry
        mes.add( createElementForField( td.getFieldByName( "gridSite" ), new NormalizedString( ) ) );
        // setup entry for stage data
        mes.add( createElementForField( td.getFieldByName( "stageData" ), new DataDescriptorT( ) ) );
        // setup data file entry
        mes.add( createElementForField( td.getFieldByName( "dataFile" ), new NormalizedString( ) ) );
        // setup meta data file entry
        mes.add( createElementForField( td.getFieldByName( "metadataFile" ), new NormalizedString( ) ) );

        orq.set_any( mes.toArray( new MessageElement[0] ) );

        return orq;
    }


    public static URI getInterSliceTransferURI( ) {

        return getURI( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
    }

    public static InterSliceTransferORQT createEmptyInterSliceTransferORQT( ) throws SOAPException {

        InterSliceTransferORQT orq = new InterSliceTransferORQT();
        orq.setOfferType( getInterSliceTransferURI() );

        ArrayList<MessageElement> mes = new ArrayList( 3 );

        TypeDesc td = InterSliceTransferORQT.getTypeDesc();
        mes.add( createElementForField( td.getFieldByName( "sourceSlices" ), new NormalizedString( ) ) );
        mes.add( createElementForField( td.getFieldByName( "destinationSlice" ), new DataDescriptorT( ) ) );
        mes.add( createElementForField( td.getFieldByName( "files" ), new NormalizedString( ) ) );

        orq.set_any( mes.toArray( new MessageElement[0] ) );

        return orq;
    }

    public static URI getRePublishSliceURI( ) throws SOAPException {

        return getURI( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
    }


    public static RePublishSliceORQT createEmptyRePublishSliceORQT( ) throws SOAPException {

    }


    public static URI getFileTransferURI( ) throws SOAPException {

        return getURI( GORFXConstantURIs.FILE_TRANSFER_URI );
    }
    

    public static FileTransferORQT createEmptyFileTransferORQT( ) throws SOAPException {

    }


    public static URI getLofisSetStageInURI ( ) throws SOAPException {

        return getURI( GORFXConstantURIs.LOFI_SET_STAGE_IN_URI );
    }


    public static LofiSetStageInORQT createEmptyLofisSetStageInORQT ( ) throws SOAPException {

    }


    public static URI getPinURI ( ) throws SOAPException {

        return getURI( GORFXConstantURIs.PIN_URI );
    }

    public static PinORQT createEmptyPinORQT ( ) throws SOAPException {

    }


    public static URI getRePublishLofiSetURI( ) throws SOAPException {

        return getURI( GORFXConstantURIs.RE_PUBLISH_LOFI_SET_URI );
    }

    public static RePublishLofiSetORQT createEmptyRePublishLofiSetORQT( ) throws SOAPException {

    }
    

    public static MessageElement createElementForField( FieldDesc fd, Object val ) throws SOAPException {

        if( fd == null )
            throw new IllegalStateException( "null field desc received" );

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
