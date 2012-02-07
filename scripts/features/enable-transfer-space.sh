enable_transferSpace() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:TransferSpace; \
	path:'$TRANSFER_AREA_PATH'; 
	gsiFtpPath:'$TRANSFER_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$TRANSFER_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:transfer; sliceKindMode:$RW; uniqueDirName:inter; mode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:TransferSpace; sliceKind: transfer; mode:'$ADDMODE'"
}
