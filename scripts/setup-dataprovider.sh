#!/bin/sh

# Variables in action parameters denoted using %{VARNAME} will be expanded 
# *at* *runtime* by the globus container using its regular shell environment

STAGING_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-staging.sh"

MODE=$1

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE


# Setup generic entities

env GNDMS_SCRIPT=1 `dirname $0`/setup-generic.sh $MODE


# Setup subspace and slicekinds

moni call .dspace.SetupSliceKind "sliceKind:'http://www.c3grid.de/G2/SliceKind/Staging'; sliceKindMode:RW; mode: $MODE"

moni call .dspace.SetupSubspace   'subspace:"{http://www.c3grid.de/G2/Subspace}ProviderStaging"; path:/tmp; gsiFtpPath: "gsiftp://seeker.zib.de/tmp"; visible:true; size:1000000000; mode:'$MODE
moni call .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}ProviderStaging'; sliceKind: http://www.c3grid.de/G2/SliceKind/Staging; mode:"$ADDMODE
moni call .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}ProviderStaging'; sliceKind: http://www.c3grid.de/G2/SliceKind/DMS; mode:"$ADDMODE


# Setup file transfer offer type

moni call .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/FileTransfer'; orqType: '{http://gndms.zib.de/c3grid/types}FileTransferORQT'; resType: '{http://gndms.zib.de/c3grid/types}FileTransferResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.FileTransferORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.FileTransferActionFactory; mode:"$MODE 


# Setup and configure staging offer type

moni call .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}ProviderStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}ProviderStageInResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInActionFactory; mode:"$MODE

moni call .gorfx.ConfigOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; subspace: '{http://www.c3grid.de/G2/Subspace}ProviderStaging'; sliceKind: 'http://www.c3grid.de/G2/SliceKind/Staging'; stagingCommand='$STAGING_COMMAND'"
