package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataDescriptor;

/**
 * Worker for converting data descriptor instances.
 *
 * The converter itself doesn't any real conversion, it just calls the
 * appropriate methods of the provided DataDescriptorWriter.
 * SEE DataDescriptorWriter for details.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 17:32:18
 */
public class DataDescriptorConverter extends GORFXConverterBase<DataDescriptorWriter, DataDescriptor> {


    public DataDescriptorConverter( ) {
        super( );
    }


    public DataDescriptorConverter( DataDescriptorWriter writer, DataDescriptor dataDescriptor ) {
        super( writer, dataDescriptor );
    }


    public void convert( ) {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeObjectList( NotNullStringArray( getModel().getObjectList() ) );

        
        if( getModel().hasConstraints() ) {
            DataConstraintsWriter dcw = getWriter( ).getDataConstraintsWriter( );
            getWriter().beginWritingDataConstraints( );
            DataConstraintsConverter dcc = new DataConstraintsConverter( dcw, getModel().getConstrains() );
            dcc.convert();
            getWriter().doneWritingDataConstraints( );
        } else
            getWriter().writeJustDownload( );


        if( getModel().hasDataFormat() )
            getWriter().writeDataFormat( NotNullString( getModel().getDataFormat() ) );

        if( getModel().hasDataArchiveFormat() )
            getWriter().writeDataArchiveFormat( getModel().getDataArchiveFormat() );

        getWriter().writeMetaDataFormat( NotNullString( getModel().getMetaDataFormat() ) );

        if( getModel().hasMetaDataArchiveFormat() )
            getWriter().writeMetaDataArchiveFormat( getModel().getMetaDataArchiveFormat() );

        getWriter().done();
    }
}