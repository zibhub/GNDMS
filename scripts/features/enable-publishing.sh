enable_publishing() {
RW="700"

moni call -v .dspace.SetupSliceKind "sliceKind:'PublishingKind'; sliceKindMode:$RW; uniqueDirName:RW; timeToLive: $TTL; mode: $MODE"

moni call -v .dspace.SetupSubspace  "subspace:'Publishing'; \
	path:'$PUBLISHING_AREA_PATH'; 
	gsiFtpPath:'$PUBLISHING_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$PUBLISHING_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'Publishing'; sliceKind: PublishingKind; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS_RW; mode:'$ADDMODE'"

#moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}ProviderStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}ProviderStageInResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInActionFactory; mode:'$MODE'"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'PublishingTaskFlow'; \
	cfgOutFormat: 'PRINT_OK'; \
	subspace: 'Publishing'; \
	sliceKind: 'PublishingKind'; \
	publisherName: '$PUBLISHING_NAME'; \
	scriptIoFormat: '$SCRIPT_IO_FORMAT'; \
	oidPrefix: '$PUBLISHING_OIDPREFIX'; \
	updateInterval: '$UPDATE_INTERVAL'"
}
# vim:tw=0
