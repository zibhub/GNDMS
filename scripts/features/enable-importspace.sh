enable_importSpace() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:ImportSpace; \
	path:'$IMPORT_AREA_PATH'; 
	gsiFtpPath:'$IMPORT_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$IMPORT_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:import; sliceKindMode:$RW; uniqueDirName:inter; timeToLive: $TTL; mmode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:ImportSpace; sliceKind: import; mode:'$ADDMODE'"

moni call -v .vold.VolDRegistrar "siteType: 'IMPORT';\
	siteName: '$IMPORT_NAME'; \
	updateInterval: $UPDATE_INTERVAL"
}

