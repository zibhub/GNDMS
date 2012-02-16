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


import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.interslicetransfer.client.InterSliceTransferMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Order type class for an inter slice transfer.
 *
 * This is basically a file transfer order, however it can be more:
 *
 * Depending if you provide {@link #sourceUri}, {@link #sourceSlice},
 * {@link #destinationUri} or {@link #destinationSpecifier} the  taskflow
 * behaves differently:
 *
 * If {@link #sourceUri} and {@link #destinationUri} are provided it
 * behaves exactly like a filetransfer.
 *
 * Given {@link #sourceUri} and {@link #destinationSpecifier}: files from the
 * source are imported into GNDMS. 
 *
 * Given {@link #sourceSlice} and {@link #destinationUri}: files from the
 * source slice are exported to the destinationUri.
 *
 * Given {@link #sourceSlice} and {@link #destinationSpecifier}: files from the
 * source Slice will be copied to the given destination.
 *
 * \note that *Uri types override Specifier resp Slice types. E.g. if
 * both sourceUri and sourceSlice are given, file will be copied from
 * sourceUri.
 *
 *
 * NOTE: There is no matching Result for this one, 
 * cause it uses the FileTransferResult and manipulates the OfferType.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 15:10:34
 */
public class InterSliceTransferOrder extends FileTransferOrder {

    private static final long serialVersionUID = -5949532448235655424L;

    private Specifier<Void> sourceSlice;  ///< Specifier for the source slice.

    /**
     * Specifier for the destination.
     *
     * It can either be a specifier to an existing slice, then the
     * files from source{Slice,Uri} will be copied into this Slice
     * possibly overwriting existing files.
     *
     * Or a specifier to an Subspace, including the desired SliceKind,
     * then a new Slice will be created.
     *
     * \note for the time being it is not possible to provide any
     * custom slice attributes, the slice will be created using
     * default values.
     */
    private Specifier<Void> destinationSpecifier; 


    public InterSliceTransferOrder() {
        super( );
        super.setTaskFlowType( InterSliceTransferMeta.INTER_SLICE_TRANSFER_URI );
    }

    @NotNull
    public String getDescription() {
        return "Transferring Files between slices";
    }


    public Specifier<Void> getSourceSlice() {
        return sourceSlice;
    }


    public void setSourceSlice( Specifier<Void> sourceSlice ) {
        this.sourceSlice = sourceSlice;
    }


    public Specifier<Void> getDestinationSpecifier() {
        return destinationSpecifier;
    }


    public void setDestinationSpecifier( Specifier<Void> destinationSpecifier ) {
        this.destinationSpecifier = destinationSpecifier;
    }
}
