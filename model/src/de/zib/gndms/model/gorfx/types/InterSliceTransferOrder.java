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



import de.zib.gndms.model.dspace.types.SliceRef;
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

    private SliceRef sourceSlice;
    private SliceRef destinationSlice;


    public InterSliceTransferOrder() {
        super( );
        super.setTaskFlowType( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
    }

    @NotNull
    public String getDescription() {
        return "Transferring Files between slices";
    }


    public SliceRef getSourceSlice() {
        return sourceSlice;
    }


    public void setSourceSlice( SliceRef sourceSlice ) {
        this.sourceSlice = sourceSlice;
    }


    public SliceRef getDestinationSlice() {
        return destinationSlice;
    }


    public void setDestinationSlice( SliceRef destinationSlice ) {
        this.destinationSlice = destinationSlice;
    }
}
