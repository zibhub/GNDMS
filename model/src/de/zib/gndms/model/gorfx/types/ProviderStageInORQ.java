package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.jetbrains.annotations.NotNull;


/**
 * Model class for a provider stage in orq.
 *
 * @author: try ma ik jo rr a zib
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
