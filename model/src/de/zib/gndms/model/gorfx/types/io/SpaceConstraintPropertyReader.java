package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SpaceConstraint;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 13:42:12
 */
public class SpaceConstraintPropertyReader extends AbstractPropertyReader<SpaceConstraint> {

    public SpaceConstraintPropertyReader() {
        super( SpaceConstraint.class );
    }


    public SpaceConstraintPropertyReader( Properties properties ) {
        super( SpaceConstraint.class, properties );
    }


    public void read() {

        getProduct().setLatitude(
                PropertyReadWriteAux.readMinMaxPair(
                    getProperties(),
                    SfrProperty.LAT_MIN.key,
                    SfrProperty.LAT_MAX.key
                )
            );

        getProduct().setLongitude(
                PropertyReadWriteAux.readMinMaxPair(
                    getProperties(),
                    SfrProperty.LON_MIN.key,
                    SfrProperty.LON_MAX.key
                )
            );
        
        getProduct( ).setAltitude(
            LevelRangePropertyReader.readLevelRange( getProperties() ) );
    }


    public static SpaceConstraint readSpaceConstraint( Properties prop ) {

        SpaceConstraintPropertyReader sc = new SpaceConstraintPropertyReader( prop );
        sc.begin( );
        sc.read( );
        return sc.getProduct( );
    }

    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
