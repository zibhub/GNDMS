package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataDescriptor;
import de.zib.gndms.model.gorfx.types.SpaceConstraint;

import java.util.HashMap;

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

        SpaceConstraint sc = getModel().getSpaceConstraint();
        if( sc == null )
            sc = new SpaceConstraint();
        SpaceConstraintWriter scw = getWriter().getSpaceConstraintWriter();
        SpaceConstraintConverter scc = new SpaceConstraintConverter( scw, sc );
        scc.convert();
        

        if( getModel().getTimeConstraint() != null )
            getWriter().writeTimeConstraint( getModel().getTimeConstraint() );

        getWriter().writeCFList( NotNullStringArray( getModel().getCFList() ) );

        HashMap<String,String> hm = getModel().getConstraintList();
        if( hm == null )
            hm = new HashMap<String,String>( );
        getWriter().writeConstraintList( hm );

        getWriter().writeDataFormat( NotNullString( getModel().getDataFormat() ) );
        getWriter().writeMetaDataFormat( NotNullString( getModel().getMetaDataFormat() ) );

        getWriter().done();
    }
}
