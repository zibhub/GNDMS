package de.zib.gndms.stuff.propertytree;

import java.util.Properties;
import java.util.Set;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 06.01.2009, Time: 17:30:53
 */
public class PropertyTreeFactory {

    public static PropertyTree createPropertyTree( Properties props ) {

        PropertyTree pt = new PropertyTree( );
        
        Set<Object> keys = props.keySet( );
        for( Object o: keys ) {
            String s = (String) o;
            pt.setProperty( s, props.getProperty( s ));
        }

        return pt;
    }


    public static PropertyTree createPropertyTree( File f ) throws IOException {

        InputStream is = null;
        try{
            is = new FileInputStream( f );
            Properties props = new Properties( );
            props.load( is );
            return createPropertyTree( props );
        } finally {
            if( is != null )
                is.close();
        }
    }
}
