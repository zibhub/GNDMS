package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;


/**
 * Model class for a provider stage in orq.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:55:45 PM
 */
public class ProviderStageInORQ extends AbstractORQ {

    private static final long serialVersionUID = -5318593349599919368L;

    private DataDescriptor dataDescriptor;

    private String actDataFile;
    private String actMetadataFile;
	private String actSliceId;


	public ProviderStageInORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
    }


    @Override
    public @NotNull String getDescription() {
        return "Providerstaging";
    }


    public DataDescriptor getDataDescriptor() {
        return dataDescriptor;
    }


    public void setDataDescriptor( DataDescriptor dataDescriptor ) {
        this.dataDescriptor = dataDescriptor;
    }


    public boolean hasDataFile( ) {
        return actDataFile != null;
    }


    public String getActDataFile() {
        return actDataFile;
    }


    public void setActDataFile( String dataFile ) {
        this.actDataFile = dataFile;
    }


    public String getActMetadataFile() {
        return actMetadataFile;
    }


    public void setActMetadataFile( String metadataFile ) {
        this.actMetadataFile = metadataFile;
    }


	public String getActSliceId() {
		return actSliceId;
	}


	public void setActSliceId(final String actSliceIdParam) {
		actSliceId = actSliceIdParam;
	}
}
