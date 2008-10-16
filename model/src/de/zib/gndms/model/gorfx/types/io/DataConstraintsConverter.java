package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataConstraints;
import de.zib.gndms.model.gorfx.types.SpaceConstraint;

import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:27:25
 */
public class DataConstraintsConverter extends GORFXConverterBase<DataConstraintsWriter, DataConstraints> {

    public DataConstraintsConverter() {
    }


    public DataConstraintsConverter( DataConstraintsWriter writer, DataConstraints model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        
        SpaceConstraint sc = getModel().getSpaceConstraint();
        if( sc == null )
            sc = new SpaceConstraint();
        SpaceConstraintWriter scw = getWriter().getSpaceConstraintWriter();
        getWriter( ).beginWritingSpaceConstraint();
        SpaceConstraintConverter scc = new SpaceConstraintConverter( scw, sc );
        scc.convert();
        getWriter( ).doneWritingSpaceConstraint();


        if( getModel().hasTimeConstraint() )
            getWriter().writeTimeConstraint( getModel().getTimeConstraint() );

        getWriter().writeCFList( NotNullStringArray( getModel().getCFList() ) );

        HashMap<String,String> hm = getModel().getConstraintList();
        if( hm == null )
            hm = new HashMap<String,String>( );
        getWriter().writeConstraintList( hm );
        getWriter().done( );
    }
}
