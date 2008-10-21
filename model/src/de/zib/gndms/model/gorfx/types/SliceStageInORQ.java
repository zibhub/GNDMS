package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 11:17:06
 * To change this template use File | Settings | File Templates.
 */
public class SliceStageInORQ extends AbstractORQ{

    private DataDescriptor dataDescriptor;
    private String gridSite;
    private String dataFile;
    private String metadataFile;

    public SliceStageInORQ( ){
        super();
        super.setOfferType( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }

    @Override
    public @NotNull String getDescription() {
        return "Slicestaging";
    }

    public DataDescriptor getDataDescriptor() {
        return dataDescriptor;
    }


    public void setDataDescriptor( DataDescriptor dataDescriptor ) {
        this.dataDescriptor = dataDescriptor;
    }

    public String getDataFile() {
        return dataFile;
    }


    public void setDataFile( String dataFile ) {
        this.dataFile = dataFile;
    }


    public String getMetadataFile() {
        return metadataFile;
    }


    public void setMetadataFile( String metadataFile ) {
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

