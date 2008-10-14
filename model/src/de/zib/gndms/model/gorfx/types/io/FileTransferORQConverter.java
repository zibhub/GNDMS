package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.FileTransferORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 14:48:27
 */
public class FileTransferORQConverter extends ORQConverter<FileTransferORQWriter, FileTransferORQ> {

    public FileTransferORQConverter() {
    }


    public FileTransferORQConverter( FileTransferORQWriter writer, FileTransferORQ model ) {
        super( writer, model );
    }


    public void convert() {
        super.convert();    //To change body of overridden methods use File | Settings | File Templates.
        getWriter( ).writeSourceURI( getModel().getSourceURI() );
        getWriter( ).writeDestinationURI( getModel().getTargetURI() );
        if( getModel( ).hasFileMap( ) )
            getWriter( ).writeFileMap( getModel().getFileMap() );

        getWriter().done( );
    }
}
