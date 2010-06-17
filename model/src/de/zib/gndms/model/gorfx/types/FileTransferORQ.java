package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

/**
 * An ORQ for file transfer.
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 29.09.2008, Time: 17:49:09
 */
public class FileTransferORQ extends AbstractORQ {

    private static final long serialVersionUID = 2695933268050863494L;

    /**
     * URI of the source files
     */
    private String sourceURI;

    /**
     * URI of the target files
     */
    private String targetURI;

    /**
     * The map consists of pairs of source and target file names.
     * The target file name may be null, if it should be identical to the source files name.
     */ 
    private TreeMap<String,String> fileMap;

    public FileTransferORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.FILE_TRANSFER_URI );
    }


    @Override
    public @NotNull String getDescription() {
        return "File transfer from " + sourceURI + " to " + targetURI;
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


    public boolean hasFileMap( ) {
        return  fileMap != null;
    }
    

    public TreeMap<String, String> getFileMap() {
        return fileMap;
    }


    public void setFileMap( TreeMap<String, String> fileMap ) {
        this.fileMap = fileMap;
    }
}
