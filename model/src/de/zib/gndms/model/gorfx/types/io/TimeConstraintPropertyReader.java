package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.TimeConstraint;
import org.joda.time.DateTime;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 14:25:59
 */
public class TimeConstraintPropertyReader extends AbstractPropertyReader<TimeConstraint> {

    public TimeConstraintPropertyReader( ) {
        super( TimeConstraint.class );
    }


    public TimeConstraintPropertyReader( Properties properties ) {
        super( TimeConstraint.class, properties );
    }


    public void read() {

        // DateTime min = PropertyReadWriteAux.readISODateTime( getProperties(), SfrProperty.TIME_MIN.key );
         String min = getProperties().getProperty( SfrProperty.TIME_MIN.key );

        // DateTime max = PropertyReadWriteAux.readISODateTime( getProperties(), SfrProperty.TIME_MAX.key );
        String max = getProperties().getProperty( SfrProperty.TIME_MAX.key );

        if( min == null && max == null ) {
            deleteProduct( );
        } else {
            getProduct( ).setMinTime( min );
            getProduct( ).setMaxTime( max );
        }
    }


    public static TimeConstraint readTimeConstraint( Properties prop ) {
        TimeConstraintPropertyReader tcr = new TimeConstraintPropertyReader( prop );
        tcr.begin( );
        tcr.read( );
        return tcr.getProduct( );
    }

    public void done() {

    }
}
