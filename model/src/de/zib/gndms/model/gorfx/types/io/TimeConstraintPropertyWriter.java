package de.zib.gndms.model.gorfx.types.io;

import org.joda.time.DateTime;

import java.util.Properties;

/**
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 14:57:16
 */
public class TimeConstraintPropertyWriter extends AbstractPropertyIO implements TimeConstraintWriter {

    public TimeConstraintPropertyWriter() {
        super( );
    }


    public TimeConstraintPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeMinTime( DateTime dt ) {
        writeMinTimeToProperties( getProperties( ), dt );
    }


    public void writeMaxTime( DateTime dt ) {
        writeMaxTimeToProperties( getProperties( ), dt );
    }


    public static void writeMinTimeToProperties( Properties prop, DateTime dt  ) {
       PropertyReadWriteAux.writeISODateTime( prop, SfrProperty.TIME_MIN.key, dt );
    }
    

    public static void writeMinTimeToProperties( Properties prop, String dt  ) {
        prop.setProperty( SfrProperty.TIME_MIN.key, dt );
    }


    public static void writeMaxTimeToProperties( Properties prop, DateTime dt ) {
        PropertyReadWriteAux.writeISODateTime( prop, SfrProperty.TIME_MAX.key, dt );
    }


    public static void writeMaxTimeToProperties( Properties prop, String dt  ) {
        prop.setProperty( SfrProperty.TIME_MAX.key, dt );
    }


    public void done() {
    }
}
