package de.zib.gndms.taskflows.interslicetransfer.client.model;

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



import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import org.jetbrains.annotations.NotNull;

/**
 * ORQ type class for an inter slice transfer.
 *
 * NOTE: There is no matching Result for this one, cause it uses the FileTransferResult and manipulates the OfferType.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 15:10:34
 */
public class InterSliceTransferOrder extends FileTransferOrder {

    private static final long serialVersionUID = -5949532448235655424L;

    private String sourceSlice;
    private String destinationSlice;

    public InterSliceTransferOrder() {
        super( );
        super.setTaskFlowType( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
    }

    @NotNull
    public String getDescription() {
        return "Transferring Files between slices";
    }


    public String getSourceSlice() {
        return sourceSlice;
    }


    public void setSourceSlice( String sourceSlice ) {
        this.sourceSlice = sourceSlice;
    }


    public String getDestinationSlice() {
        return destinationSlice;
    }


    public void setDestinationSlice( String destinationSlice ) {
        this.destinationSlice = destinationSlice;
    }
}
