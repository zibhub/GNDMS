#!/bin/sh

enable_providerstagein() {
RW="700"

moni call -v .dspace.SetupSliceKind "sliceKind:'ProviderKind'; sliceKindMode:$RW; uniqueDirName:RW; timeToLive: $TTL; mode: $MODE"

moni call -v .dspace.SetupSubspace  "subspace:'ProviderStaging'; \
	path:'$STAGING_AREA_PATH'; 
	gsiFtpPath:'$STAGING_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$STAGING_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: ProviderKind; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS; mode:'$ADDMODE'"
#moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind: DMS_RW; mode:'$ADDMODE'"

#moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}ProviderStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}ProviderStageInResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInActionFactory; mode:'$MODE'"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'ProviderStageIn'; \
	cfgOutFormat: 'PRINT_OK'; \
	subspace: 'ProviderStaging'; \
	sliceKind: 'ProviderKind'; \
	estimationClass: 'de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInQuoteCalculator'; \
	stagingClass: 'de.zib.gndms.taskflows.staging.server.logic.ExternalProviderStageInAction'; \
	stagingCommand: '$STAGING_COMMAND'; \
	estimationCommand: '$ESTIMATION_COMMAND'; \
	cancelCommand: '$CANCEL_COMMAND'; \
	scriptIoFormat: '$SCRIPT_IO_FORMAT'; \
	oidPrefixe: '$STAGING_OIDPREFIXE'; \
	updateInterval: '$UPDATE_INTERVAL'"
}
# vim:tw=0
