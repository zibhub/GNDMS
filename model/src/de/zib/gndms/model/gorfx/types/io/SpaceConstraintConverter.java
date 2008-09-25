package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SpaceConstraint;

/**
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
        if( getModel().hasLatitude() )
            getWriter().writeLatitude( getModel().getLatitude() );

        if( getModel().hasLongitude() )
            getWriter().writeLongitude( getModel().getLongitude() );

        if( getModel().hasAltitude() ) {
            LevelRangeWriter lrw = getWriter().getLevelRangeWriter( );
            LevelRangeConverter conv = new LevelRangeConverter(  lrw, getModel().getAltitude() );
            conv.convert( );
        }
        getWriter().done();
    }
}
