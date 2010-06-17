enable_ptresource() {
RW="700"

moni call -v .dspace.SetupSliceKind "sliceKind:'http://www.ptgrid.de/G1/SliceKind/Staging_RW'; sliceKindMode:$RW; uniqueDirName:RW; mode: $MODE"

moni call -v .dspace.SetupSubspace  "subspace:'{http://www.ptgrid.de/G1/Subspace}Staging'; \
	path:'$STAGING_AREA_PATH'; 
	gsiFtpPath:'$STAGING_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$STAGING_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'{http://www.ptgrid.de/G1/Subspace}Staging'; sliceKind: http://www.ptgrid.de/G1/SliceKind/Staging_RW; mode:'$ADDMODE'"


