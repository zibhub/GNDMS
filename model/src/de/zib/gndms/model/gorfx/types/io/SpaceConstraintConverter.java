package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SpaceConstraint;

/**
 *
 * @see SpaceConstraintWriter
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 10:40:12
 */
public class SpaceConstraintConverter extends GORFXConverterBase<SpaceConstraintWriter, SpaceConstraint> {


    public SpaceConstraintConverter() {
    }


    public SpaceConstraintConverter( SpaceConstraintWriter writer, SpaceConstraint model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeLatitude( getModel().getLatitude() );

        getWriter().writeLongitude( getModel().getLongitude() );

        if( getModel().hasAreaCRS() )
            getWriter().writeAreaCRS( getModel().getAreaCRS() );

        if( getModel().hasAltitude() )
            getWriter().writeAltitude( getModel().getAltitude() );

        if( getModel().hasVerticalCRS() )
            getWriter().writeVerticalCRS( getModel().getVerticalCRS() );
            
        getWriter().done();
    }
}
