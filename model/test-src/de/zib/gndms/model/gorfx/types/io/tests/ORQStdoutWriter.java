package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.io.ORQWriter;

import java.util.HashMap;
import java.util.Set;

/**
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
        Set<String> ks = ctx.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + ctx.get( k ) );

    }
}
