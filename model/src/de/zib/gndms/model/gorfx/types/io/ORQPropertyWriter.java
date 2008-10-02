package de.zib.gndms.model.gorfx.types.io;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:23:58
 */
public abstract class ORQPropertyWriter extends AbstractPropertyIO implements ORQWriter {

    protected ORQPropertyWriter() {
    }


    protected ORQPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeJustEstimate( boolean je ) {

        getProperties( ).setProperty( SfrProperty.JUST_ASK.key, Boolean.toString( je ) );
    }


    public void read() {
        
    }
}
