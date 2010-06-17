package de.zib.gndms.model.gorfx.types.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Writes an ORQ to Stdout.
 * It should be used in conjunction with an ORQConverter.
 *
 *
 * @see de.zib.gndms.model.gorfx.types.io.ORQConverter
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:57:12
 */
public abstract class ORQStdoutWriter implements ORQWriter {

    public void writeJustEstimate( boolean je ) {
        System.out.println( "Just estimate: " + Boolean.toString( je ) );
    }


    public void writeContext( HashMap<String, String> ctx ) {
        System.out.println( "Context" );
        showMap( ctx );
    }


    public static void showMap( Map<String, String> map ) {
        Set<String> ks = map.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + map.get( k ) );
    }


    public void writeId( String id ) {
        System.out.println( "GORFXId: " + id);
    }
}
