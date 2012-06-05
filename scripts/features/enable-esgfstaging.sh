enable_esgfstageing() {
RW="700"

moni call -v .dspace.SetupSliceKind "sliceKind:'esgfKind'; sliceKindMode:$RW; uniqueDirName:RW; mode: $MODE"

moni call -v .dspace.SetupSubspace  "subspace:'esgfStaging'; \
	path:'$ESGF_STAGING_AREA_PATH'; 
	gsiFtpPath:'$ESGF_STAGING_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$ESGF_STAGING_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'esgfStaging'; sliceKind: esgfKind; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS_RW; mode:'$ADDMODE'"

#moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}ProviderStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}ProviderStageInResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInActionFactory; mode:'$MODE'"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'ESGFStagingTaskFlow'; \
	cfgOutFormat: 'PRINT_OK'; \
	subspace: 'esgfStaging'; \
	sliceKind: 'esgfKind'; \
	trustStoreLocation: '$ESGF_TRUSTSTORE'; \
	trustStorePassword: '$ESGF_TRUSTSTORE_PASSWORD'; \
	estimationClass: 'de.zib.gndms.taskflows.esgfStaging.server.ESGFStagingQuoteCalculator'; \
	stagingClass: 'de.zib.gndms.taskflows.esgfStaging.server.ESGFStagingTFAction'"
}
# vim:tw=0
