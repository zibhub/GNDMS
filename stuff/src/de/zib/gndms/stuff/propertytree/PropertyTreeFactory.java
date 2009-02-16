package de.zib.gndms.stuff.propertytree;

import java.util.Properties;
import java.util.Set;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This factory class creates a {@code PropertyTree} out of a {@code File} or {@code Properties} object.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 06.01.2009, Time: 17:30:53
 */
public class PropertyTreeFactory {
    /**
     * Creates a {@code PropertyTree} from a {@code Properties} object.
     * @param props the properties to be converted into a {@code PropertyTree}
     * @return the {@code Properties} object as an {@code PropertyTree}
     */
    public static PropertyTree createPropertyTree( Properties props ) {

        PropertyTree pt = new PropertyTree( );
        
        Set<Object> keys = props.keySet( );
        for( Object o: keys ) {
            String s = (String) o;
            pt.setProperty( s, props.getProperty( s ));
        }

        return pt;
    }

    /**
     * Creates a {@code PropertyTree} from a file.
     * See {@link Properties#load(java.io.InputStream)} about the expected file format.
     * @param f a {@code File}-object containg the properties
     * @return a {@code PropertyTree} from the {@code File} object
     * @throws IOException if an problem occurs reading the file
     */
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
