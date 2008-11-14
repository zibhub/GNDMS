#!/bin/sh

# %{} is shell variable substitution at container runtime

STAGING_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-staging.sh"
ESTIMATION_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-estimation.sh"
STAGING_AREA_PATH="/tmp"
STAGING_AREA_SIZE="1000000" # Currently unused
# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"


source $(dirname $0)/check-hostname.sh
# One can set the $hn variable manually in the check-hostname script,
# if the returned value isn't the desired one.
STAGING_AREA_GSI_FTP_URL="gsiftp://$hn""$STAGING_AREA_PATH"



# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

MODE=$1

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE


# Setup generic entities

env GNDMS_SCRIPT=1 `dirname $0`/setup-generic.sh $MODE


# Variables in action parameters denoted using %{VARNAME} will be expanded 
# *at* *runtime* by the globus container using its regular shell environment

# Setup subspace and slicekinds

moni call -v .dspace.SetupSliceKind "sliceKind:'http://www.c3grid.de/G2/SliceKind/Staging'; sliceKindMode:RW; mode: $MODE"

moni call -v .dspace.SetupSubspace   "subspace:'{http://www.c3grid.de/G2/Subspace}ProviderStaging'; path:'$STAGING_AREA_PATH'; gsiFtpPath: '$STAGING_AREA_GSI_FTP_URL'; visible:true; size:'$STAGING_AREA_SIZE'; mode:'$MODE'"
moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}ProviderStaging'; sliceKind: http://www.c3grid.de/G2/SliceKind/Staging; mode:'$ADDMODE'"
moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}ProviderStaging'; sliceKind: http://www.c3grid.de/G2/SliceKind/DMS; mode:'$ADDMODE'"


# Setup file transfer offer type

moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/FileTransfer'; orqType: '{http://gndms.zib.de/c3grid/types}FileTransferORQT'; resType: '{http://gndms.zib.de/c3grid/types}FileTransferResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.FileTransferORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.FileTransferActionFactory; mode:'$MODE'"


# Setup and configure staging offer type

moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}ProviderStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}ProviderStageInResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInActionFactory; mode:'$MODE'"

moni call -v .gorfx.ConfigOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; \
cfgOutFormat: 'PRINT_OK'; \
subspace: '{http://www.c3grid.de/G2/Subspace}ProviderStaging'; \
sliceKind: 'http://www.c3grid.de/G2/SliceKind/Staging'; \
stagingClass: 'de.zib.gndms.logic.model.gorfx.c3grid.ExternalProviderStageInAction'; \
estimationClass: 'de.zib.gndms.logic.model.gorfx.c3grid.ExternalProviderStageInORQCalculator'; \
stagingCommand='$STAGING_COMMAND'; \
estimationCommand='$ESTIMATION_COMMAND'; \
scriptIoFormat='$SCRIPT_IO_FORMAT' '"
