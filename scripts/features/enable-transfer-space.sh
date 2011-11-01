enable_transferSpace() {
RW="700"

moni call -v .dspace.SetupSliceKind "sliceKind:'http://www.c3grid.de/G2/SliceKind/transfer'; sliceKindMode:$RW; uniqueDirName:inter; mode: $MODE"

moni call -v .dspace.SetupSubspace  "subspace:'{http://www.c3grid.de/G2/Subspace}TransferSpace'; \
	path:'$TRANSFER_AREA_PATH'; 
	gsiFtpPath:'$TRANSFER_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$TRANSFER_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}TransferSpace'; sliceKind: http://www.c3grid.de/G2/SliceKind/transfer; mode:'$ADDMODE'"
}
