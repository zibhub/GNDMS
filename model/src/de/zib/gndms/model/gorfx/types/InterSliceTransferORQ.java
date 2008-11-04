package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.model.dspace.types.SliceRef;

import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 15:10:34
 */
public class InterSliceTransferORQ extends FileTransferORQ {

    private static final long serialVersionUID = -5949532448235655424L;

    private SliceRef sourceSlice;
    private SliceRef destinationSlice;


    public InterSliceTransferORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
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
