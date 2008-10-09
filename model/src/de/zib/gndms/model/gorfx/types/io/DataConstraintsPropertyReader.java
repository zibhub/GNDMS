package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataConstraints;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 16:09:55
 */
public class DataConstraintsPropertyReader extends AbstractPropertyReader<DataConstraints> {

    public DataConstraintsPropertyReader( ) {
        super( DataConstraints.class );
    }


    public DataConstraintsPropertyReader( Properties properties ) {
        super( DataConstraints.class, properties );
    }


    @Override
    public void read() {

        getProduct( ).setSpaceConstraint( SpaceConstraintPropertyReader.readSpaceConstraint( getProperties() ) );

        getProduct( ).setTimeConstraint( TimeConstraintPropertyReader.readTimeConstraint( getProperties() ) );

        // read cfl
        String [] cfl =  PropertyReadWriteAux.readListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key );
        if( cfl == null )
            cfl = PropertyReadWriteAux.readList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ' );
        getProduct( ).setCFList( cfl );

        getProduct( ).setConstraintList(
            PropertyReadWriteAux.readMap( getProperties(), SfrProperty.CONSTRAINT_LIST.key ) );
    }


    public void done() {
        // Not required here
    }


    public static DataConstraints readDataConstraints( Properties prop ) {

        DataConstraintsPropertyReader dc = new DataConstraintsPropertyReader( prop );
        dc.performReading();
        return dc.getProduct( );
    }
}
