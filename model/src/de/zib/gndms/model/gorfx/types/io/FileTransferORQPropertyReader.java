package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.FileTransferORQ;

import java.util.Properties;
import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:19:06
 */
public class FileTransferORQPropertyReader extends ORQPropertyReader<FileTransferORQ>{

    public FileTransferORQPropertyReader() {
        super( FileTransferORQ.class );
    }


    public FileTransferORQPropertyReader( Properties properties ) {
        super( FileTransferORQ.class, properties );
    }


    @Override
    public void read( ) {

        super.read( );
        getProduct().setSourceURI( getProperties().getProperty(
            SfrProperty.FILE_TRANSFER_SOURCE_URI.key ) );
        getProduct().setTargetURI( getProperties().getProperty(
            SfrProperty.FILE_TRANSFER_DESTINATION_URI.key ) );

        getProduct().setFileMap(
            new TreeMap<String, String> (
                PropertyReadWriteAux.readMap( getProperties(), SfrProperty.FILE_TRANSFER_FILE_MAPPING.key )
            )
        );

    }
    

    public void done() {
        // Not required here
    }
}
