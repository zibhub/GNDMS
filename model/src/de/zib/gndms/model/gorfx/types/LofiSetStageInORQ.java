package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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
