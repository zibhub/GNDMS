package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:32:16
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQ extends AbstractORQ{

    private DataDescriptor dataDescriptor;
    private String dataFile;
    private String metadataFile;
    private String gridSite;


    public LofiSetStageInORQ(){
        super();
        super.setOfferType(GORFXConstantURIs.LOFI_SET_STAGE_IN_URI);
    }

    @Override
    public @NotNull String getDescription() {
        return "LofiSetstaging";
    }


    public String getMetadataFile() {
        return metadataFile;
    }

    public String getDataFile() {
        return dataFile;
    }

    public DataDescriptor getDataDescriptor() {
        return dataDescriptor;
    }

    public void setDataDescriptor(DataDescriptor dataDescriptor) {
        this.dataDescriptor = dataDescriptor;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public void setMetadataFile(String metadataFile) {
        this.metadataFile = metadataFile;
    }

    public void setGridSite( String gridSite ){
        this.gridSite = gridSite;
    }

    public boolean hasGridSite(){
        return gridSite != null && gridSite.length()!=0;
    }

    public String getGridSite() {
        return gridSite;
    }
}
