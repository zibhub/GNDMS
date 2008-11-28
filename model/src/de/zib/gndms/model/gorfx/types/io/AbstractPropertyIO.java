package de.zib.gndms.model.gorfx.types.io;

import java.util.Properties;

/**
 * Base class for property readers and writers.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:17:00
 */
public abstract class AbstractPropertyIO implements GORFXWriterBase {

    private Properties properties;


    protected AbstractPropertyIO() {
    }


    protected AbstractPropertyIO( Properties properties ) {
        this.properties = properties;
    }


    public Properties getProperties() {
        return properties;
    }


    public void setProperties( Properties properties ) {
        this.properties = properties;
    }


    public void begin() {

         if( getProperties( ) == null )
             throw new IllegalStateException( "No property instance provided" );
     }


    protected String getMandatoryProperty( String key ) throws MandatoryPropertyMissingException {

        return PropertyReadWriteAux.getMandatoryProeprty( properties, key );
    }
}
