package de.zib.gndms.taskflows.staging.client.model;

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



import de.zib.gndms.common.model.gorfx.types.SliceOrder;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import org.jetbrains.annotations.NotNull;


/**
 * Model class for a provider stage in Order.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:55:45 PM
 */
public class ProviderStageInOrder extends SliceOrder {

    private static final long serialVersionUID = -5318593349599919368L;

    /**
     * The data descriptor.
     *
     * Contains the constraints for stating.
     */
    private DataDescriptor dataDescriptor;

    /**
     * The out-put file name for the stating.
     *
     * The file containing the staged data will have this name.
     */
    private String actDataFile;

    /**
     * The out-put file name for the stating meta-data.
     *
     * The file containing the meta data will have this name.
     */
    private String actMetadataFile;


    public ProviderStageInOrder() {
        super( );
        super.setTaskFlowType( ProviderStageInMeta.PROVIDER_STAGING_KEY );
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


}
