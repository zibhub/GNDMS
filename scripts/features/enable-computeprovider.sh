#!/bin/sh

enable_computeprovider() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:ComputeStaging; \
	path:'$TRANSFER_AREA_PATH'; 
	gsiFtpPath:'$TRANSFER_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$TRANSFER_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:CPKind; sliceKindMode:$RW; uniqueDirName:CP; timeToLive: $TTL; mode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:ComputeStaging; sliceKind: CPKind; mode:'$ADDMODE'"
}
