package de.zib.gndms.typecon.common.type;

import types.SliceStageInORQT;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQConverter;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.typecon.common.GORFXClientTools;

import javax.xml.soap.SOAPException;
import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 08:51:08
 */
public class SliceStageInORQXSDTypeWriter extends AbstractXSDTypeWriter<SliceStageInORQT>
    implements SliceStageInORQWriter {

    private DataDescriptorXSDTypeWriter dataWriter;


    public void writeGridSiteName( String gsn ) {
        try {
            getProduct().get_any()[0].setObjectValue( gsn );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public void writeDataFileName( String dfn ) {
        try {
            getProduct().get_any()[2].setObjectValue( dfn );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public void writeMetaDataFileName( String mfn ) {
        try {
            getProduct().get_any()[3].setObjectValue( mfn );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public DataDescriptorWriter getDataDescriptorWriter() {

        if( dataWriter == null )
            dataWriter = new DataDescriptorXSDTypeWriter();
        return dataWriter;
    }


    public void beginWritingDataDescriptor() {
        if( dataWriter == null )
            throw new IllegalStateException( "no data writer present" );
    }


    public void doneWritingDataDescriptor() {
        if( dataWriter == null )
            throw new IllegalStateException( "no data writer present" );
        try {
            getProduct().get_any()[1].setObjectValue( dataWriter.getProduct( ) );
        } catch ( SOAPException e ) {
            boxException( e );
        }
    }


    public void writeJustEstimate( boolean je ) {
        // not required in XSD context.
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // not required in XSD context.
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createEmptySliceStageInORQT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    protected void boxException( Exception e ) {
        throw new RuntimeException( e.getMessage(), e );
    }


    /**
     * On shot write resp. convert operation.
     *
     * @param orq Source provider stage in orq.
     * @return XSD version of the source orq.
     */
    public static SliceStageInORQT write( SliceStageInORQ orq ) {

        SliceStageInORQXSDTypeWriter writer = new SliceStageInORQXSDTypeWriter();
        SliceStageInORQConverter conv = new SliceStageInORQConverter( writer, orq );
        conv.convert();

        return writer.getProduct();
    }
}
