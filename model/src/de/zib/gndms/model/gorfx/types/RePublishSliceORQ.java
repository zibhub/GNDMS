package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 31.10.2008, Time: 17:10:30
 */
public class RePublishSliceORQ extends AbstractORQ{

    private static final long serialVersionUID = -3698350953236158296L;
    private String sliceId;
    private TreeMap<String,String> fileMap;


    public RePublishSliceORQ() {
        super( );
        super.setOfferType( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
    }


    public @NotNull String getDescription() {
        return "Publishing Slice " + sliceId;
    }


    public String getSliceId() {
        return sliceId;
    }


    public void setSliceId( String sliceId ) {
        this.sliceId = sliceId;
    }


    public TreeMap<String, String> getFileMap() {
        return fileMap;
    }


    public void setFileMap( TreeMap<String, String> fileMap ) {
        this.fileMap = fileMap;
    }
}
