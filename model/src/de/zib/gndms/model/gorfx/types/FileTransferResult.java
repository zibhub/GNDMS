package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 12:38:01
 */
public class FileTransferResult extends AbstractTaskResult {

    private String[] files; 

    public FileTransferResult( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.FILE_TRANSFER_URI );
    }

    public String[] getFiles() {
    return files;
}


    public void setFiles( String[] fiels ) {
        this.files = files;
    }
}
