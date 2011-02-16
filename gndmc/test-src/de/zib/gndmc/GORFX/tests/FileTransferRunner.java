package de.zib.gndmc.GORFX.tests;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQStdoutWriter;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDTypeWriter;
import org.apache.axis.message.MessageElement;
import types.FileTransferResultT;

import java.io.StringWriter;
import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
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
