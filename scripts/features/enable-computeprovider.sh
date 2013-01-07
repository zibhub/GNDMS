#!/bin/sh

enable_computeprovider() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:TransferSpace; \
	path:'$TRANSFER_AREA_PATH'; 
	gsiFtpPath:'$TRANSFER_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$TRANSFER_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:transfer; sliceKindMode:$RW; uniqueDirName:inter; timeToLive: $TTL; mmode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:TransferSpace; sliceKind: transfer; mode:'$ADDMODE'"

moni call -v .vold.VolDRegistrar "siteType: 'CP';\
	siteName: '$CP_NAME'; \
	updateInterval: $UPDATE_INTERVAL"
}
