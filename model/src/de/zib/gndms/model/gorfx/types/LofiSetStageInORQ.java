package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:32:16
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQ extends AbstractORQ {
	private static final long serialVersionUID = 2659352128685279699L;

    private DataDescriptor actDataDescriptor;
    private String actDataFile;
    private String actMetadataFile;
    private String actGridSite;


	public LofiSetStageInORQ(){
        super();
        super.setOfferType(GORFXConstantURIs.LOFI_SET_STAGE_IN_URI);
    }

    @Override
    public @NotNull String getDescription() {
        return "LofiSetstaging";
    }


    public String getActMetadataFile() {
        return actMetadataFile;
    }

    public String getActDataFile() {
        return actDataFile;
    }

    public DataDescriptor getActDataDescriptor() {
        return actDataDescriptor;
    }

    public void setActDataDescriptor(DataDescriptor dataDescriptor) {
        this.actDataDescriptor = dataDescriptor;
    }

    public void setActDataFile(String dataFile) {
        this.actDataFile = dataFile;
    }

    public void setActMetadataFile(String metadataFile) {
        this.actMetadataFile = metadataFile;
    }

    public void setActGridSite( String gridSite ){
        this.actGridSite = gridSite;
    }

    public boolean hasGridSite(){
        return actGridSite != null && actGridSite.length()!=0;
    }

    public String getActGridSite() {
        return actGridSite;
    }
}
