package de.zib.gndmc.util;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:31:14
 */
public abstract class LoadablePropertyBean {

    public abstract void setProperties( Properties prop );

    public abstract void createExampleProperties( Properties prop );

    public final void setProperties( String fn ) {

        // read property file
        InputStream f = null;
        RuntimeException exc = null;
        try {
            f = new FileInputStream( fn );
            Properties prop = new Properties( );
            prop.load( f );
            setProperties( prop );
        } catch ( FileNotFoundException e ) {
            exc =  new RuntimeException( "Failed to load properties file " + fn, e );
        } catch ( IOException e ) {
            exc =  new RuntimeException( "Failed to read properties from file " + fn, e );
        } finally {
            if( f != null )
                try {
                    f.close( );
                } catch ( IOException e ) {
                    RuntimeException re =
                        new RuntimeException( "Failed to close properties file " + fn, e );
                    if( exc != null )
                        re.initCause( exc );
                    throw re;
                }
            if( exc != null )
                throw exc;
        }
    }
}
