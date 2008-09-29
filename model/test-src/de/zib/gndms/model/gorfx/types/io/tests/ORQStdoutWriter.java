package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.io.ORQWriter;

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
}
