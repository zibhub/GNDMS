package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.io.ProviderStageInResultWriter;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.typecon.common.GORFXClientTools;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;
import types.ProviderStageInResultT;
import types.SliceReference;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import java.io.IOException;

/**
 * @author: Maik Jorra <jorra@zib.de>
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

        String s = ServiceHost.getBaseURL( ).toString( ) + "c3grid/Slice";

        SimpleResourceKey sk =
            new SimpleResourceKey( new QName("http://dspace.gndms.zib.de/DSpace/Slice", "SliceKey"), sid );
        return AddressingUtils.createEndpointReference( s, sk );
    }
}
