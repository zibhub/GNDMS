package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.io.SliceRefWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefConverter;
import de.zib.gndms.model.dspace.types.SliceRef;
import types.SliceReference;

import javax.xml.namespace.QName;

import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 14:04:02
 */
public class SliceRefXSDTypeWriter extends AbstractXSDTypeWriter<SliceReference> implements SliceRefWriter {

    // use write() for one shot writing s.b.

    public void writeSliceRef( SliceRef sf ) {

        try {

            SimpleResourceKey sk = new SimpleResourceKey(  sf.getResourceKeyName(), sf.getResourceKeyValue() );
            EndpointReferenceType epr = AddressingUtils.createEndpointReference( sf.getGridSiteId(), sk );

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
    }


    public void done() {
        //nothing to do
        setProduct( new SliceReference( ) );
    }


    public static SliceReference write( SliceRef sr ) {

        SliceRefXSDTypeWriter wrt = new SliceRefXSDTypeWriter();
        SliceRefConverter conv = new SliceRefConverter( wrt, sr );
        conv.convert( );
        return wrt.getProduct();
    }
}
