package de.zib.gndms.model.gorfx.types;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 29.09.2008, Time: 17:49:09
 */
public class FileTransferORQ extends AbstractORQ {

    private String sourceURI;
    private String targetURI;
    private TreeMap<String,String> FileMap;

    public FileTransferORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.FILE_TRANSFER_URI );
    }


    public String getSourceURI() {
        return sourceURI;
    }


    public void setSourceURI( String sourceURI ) {
        this.sourceURI = sourceURI;
    }


    public String getTargetURI() {
        return targetURI;
    }


    public void setTargetURI( String targetURI ) {
        this.targetURI = targetURI;
    }


    public TreeMap<String, String> getFileMap() {
        return FileMap;
    }


    public void setFileMap( TreeMap<String, String> fileMap ) {
        FileMap = fileMap;
    }
}
