#!/bin/sh

enable_computeAndDataprovider() {
RW="700"

moni call -v .dspace.SetupSubspace  "subspace:'ProviderStaging'; \
	path:'$STAGING_AREA_PATH'; 
	gsiFtpPath:'$STAGING_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$STAGING_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.SetupSliceKind "sliceKind:'CPKind'; sliceKindMode:$RW; uniqueDirName:CP; timeToLive: $TTL; mmode: $MODE"
moni call -v .dspace.SetupSliceKind "sliceKind:'ProviderKind'; sliceKindMode:$RW; uniqueDirName:RW; timeToLive: $TTL; mode: $MODE"

moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind:'CPKind'; mode:'$ADDMODE'"
moni call -v .dspace.AssignSliceKind "subspace:'ProviderStaging'; sliceKind:'ProviderKind'; mode:'$ADDMODE'"

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

moni call -v .vold.VolDRegistrar "siteType: 'CP';\
	siteName: '$CP_NAME'; \
	updateInterval: $UPDATE_INTERVAL"
}
