package de.zib.gndms.model.gorfx.types.io;

import java.util.Map;
import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 16:46:13
 */
public class FileTransferORQPropertyWriter extends ORQPropertyWriter implements FileTransferORQWriter {

    public FileTransferORQPropertyWriter() {
    }


    public FileTransferORQPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeSourceURI( String uri ) {
        getProperties().setProperty( SfrProperty.FILE_TRANSFER_SOURCE_URI.key, uri );
    }


    public void writeDestinationURI( String uri ) {
        getProperties().setProperty( SfrProperty.FILE_TRANSFER_DESTINATION_URI.key, uri );
    }


    public void writeFileMap( Map<String, String> fm ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.FILE_TRANSFER_FILE_MAPPING.key, fm );
    }


    public void done() {
        // Not required here
    }
}
