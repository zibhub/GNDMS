#!/bin/sh

MODE=$1

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE


# Setup generic entities

env GNDMS_SCRIPT=1 `dirname $0`/setup-generic.sh $MODE


# Setup subspace and slicekinds

moni call .dspace.SetupSliceKind "uri:'http://www.c3grid.de/G2/SliceKind/Staging'; sliceKindMode:RW; mode: $MODE"

moni call .dspace.SetupSubspace 'scope:http://www.c3grid.de/G2/Subspace/; name:ProviderStaging; path:/tmp; visible:true; size:1000000000; mode:'$MODE
moni call .dspace.AssignSliceKind "metaScope:http://www.c3grid.de/G2/Subspace/; metaName:ProviderStaging; sliceKindUri: http://www.c3grid.de/G2/SliceKind/Staging; mode:"$ADDMODE
moni call .dspace.AssignSliceKind "metaScope:http://www.c3grid.de/G2/Subspace/; metaName:ProviderStaging; sliceKindUri: http://www.c3grid.de/G2/SliceKind/DMS; mode:"$ADDMODE


# Setup file transfer offer type

moni call .gorfx.SetupOfferType "key: 'http://www.c3grid.de/ORQTypes/FileTransfer'; argScope: 'http://gndms.zib.de/c3grid/types'; argName: FileTransferORQT; resScope: 'http://gndms.zib.de/c3grid/types'; resName: FileTransferResultT; class: de.zib.gndms.logic.model.gorfx.FileTransferORQCalculator; factoryClass: de.zib.gndms.kit.factory.InstanceFactory; mode:"$MODE 


# Setup and configure staging offer type

moni call .gorfx.SetupOfferType "key: 'http://www.c3grid.de/ORQTypes/ProviderStageIn'; argScope: 'http://gndms.zib.de/c3grid/types'; argName: ProviderStageInORQT; resScope: 'http://gndms.zib.de/c3grid/types'; resName: ProviderStageInResultT; class: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInORQCalculator; factoryClass: de.zib.gndms.logic.model.gorfx.c3grid.ProviderStageInFactory; mode:"$MODE

