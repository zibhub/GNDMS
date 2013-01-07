enable_exportSpace() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:ExportSpace; \
	path:'$EXPORT_AREA_PATH'; 
	gsiFtpPath:'$EXPORT_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$EXPORT_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:export; sliceKindMode:$RW; uniqueDirName:inter; timeToLive: $TTL; mmode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:ExportSpace; sliceKind: export; mode:'$ADDMODE'"

moni call -v .vold.VolDRegistrar "siteType: 'EXPORT';\
	siteName: '$EXPORT_NAME'; \
	updateInterval: $UPDATE_INTERVAL"
}
