package de.zib.gndms.GORFX.action.tests;

import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQStdoutWriter;
import de.zib.gndms.typecon.common.type.FileTransferORQXSDTypeWriter;
import org.apache.axis.message.MessageElement;
import types.FileTransferResultT;

import java.io.StringWriter;
import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.02.2009, Time: 16:10:54
 */
public class FileTransferRunner extends RequestRunner {

    private static final FileTransferORQPropertyReader reader = new FileTransferORQPropertyReader( );
    private static final FileTransferORQStdoutWriter stdout = new FileTransferORQStdoutWriter( );
    private static final FileTransferORQConverter converter = new FileTransferORQConverter( );
    private static final FileTransferORQXSDTypeWriter xsdwrt = new FileTransferORQXSDTypeWriter( );

    public FileTransferRunner( ) {
        super();
        setConverter( converter );
    }

    
    public void show() {
        show( stdout );
    }


    protected void showResult( final TaskClient tcnt ) throws Exception {
        FileTransferResultT rest  = tcnt.getExecutionResult( FileTransferResultT.class );
        MessageElement[] mes = rest.get_any();
        StringWriter sw = new StringWriter( );
        for( MessageElement me : mes )
                sw.write( "    " + ( (String) me.getObjectValue( String.class ) ) + '\n' ) ;

        getLogger().info( "Copied the following files:\n" + sw.toString() );
    }


    public void prepare() {
        prepare( xsdwrt );
    }


    public void fromProps( final Properties props ) {
        readProps( props, reader );
    }
}
