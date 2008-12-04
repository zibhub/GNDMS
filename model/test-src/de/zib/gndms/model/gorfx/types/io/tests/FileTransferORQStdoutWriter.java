package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.io.FileTransferORQWriter;

import java.util.Map;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 15:02:21
 */
public class FileTransferORQStdoutWriter extends ORQStdoutWriter implements FileTransferORQWriter {

    public void writeSourceURI( String uri ) {

        System.out.println( "SourceURI: " + uri );
    }


    public void writeDestinationURI( String uri ) {
        System.out.println( "DestinationURI: " + uri );
    }


    public void writeFileMap( Map<String, String> fm ) {
        System.out.println( "FileMap: " );
        showMap( fm );
    }


    public void begin() {
        System.out.println( "******************** FileTransferORQ ********************" );
    }


    public void done() {
        System.out.println( "******************* EOFileTransferORQ *******************" );
    }
}
