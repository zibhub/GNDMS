package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.AbstractORQ;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 18:02:08
 */
public abstract class ORQPropertyReader<M extends AbstractORQ> extends AbstractPropertyReader<M> {

    protected ORQPropertyReader( Class productClass ) {
        super( productClass );
    }


    protected ORQPropertyReader( Class productClass, Properties properties ) {
        super( productClass, properties );
    }


    public void read() {
        String s = getProperties( ).getProperty( SfrProperty.JUST_ASK.key );
        if( s != null  )
            getProduct().setJustEstimate( Boolean.parseBoolean( s ));
    }
}
