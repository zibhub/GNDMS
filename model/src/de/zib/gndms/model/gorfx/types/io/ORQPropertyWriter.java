package de.zib.gndms.model.gorfx.types.io;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.HashMap;

/**
 * Writes an ORQ as a Properties instance.
 * It should be used in conjunction with an ORQConverter.
 *
 * @see de.zib.gndms.model.gorfx.types.io.ORQConverter
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


    public void writeContext( @NotNull HashMap<String, String> ctx ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.CONTEXT.key,  ctx );
    }


    public void writeId( String id ) {
        getProperties( ).setProperty( SfrProperty.GORFX_ID.key, id );
    }
}
