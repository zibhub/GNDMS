package de.zib.gndms.taskflows.interslicetransfer.client;

import de.zib.gndms.taskflows.filetransfer.client.FileTransferMeta;

public class InterSliceTransferMeta extends FileTransferMeta {

    public static final String INTER_SLICE_TRANSFER_KEY = "InterSliceTransfer";

    @Override
    public String getDescription() {
        return "Copies slices between different subspaces";
    }
}