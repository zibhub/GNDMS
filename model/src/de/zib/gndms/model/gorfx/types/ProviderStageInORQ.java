package de.zib.gndms.model.gorfx;

/**
 * Model class for a provider stage in orq.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:55:45 PM
 */
public class ProviderStageInORQ extends AbstractORQ {

    private DataDescriptor dataDescriptor;
    private String dataFile;
    private String metadataFile;

    public ProviderStageInORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
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
}
