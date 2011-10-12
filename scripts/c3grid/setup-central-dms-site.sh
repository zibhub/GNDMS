#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

# %{} is shell variable substitution at container runtime

MDS_URL="http://c3grid.it.irf.tu-dortmund.de:8080/webmds/webmds?info=indexinfo"
# old: MDS_URL="http://c3grid-gt.e-technik.uni-dortmund.de:8080/webmds/webmds?info=indexinfo"
MDS_PREFIX="g2."
DMS_AREA_PATH="/tmp"
DMS_AREA_SIZE="10000" # Unused

# One can set the $GRIDHOST variable manually
# if the detected value isn't the desired one
# GRIDHOST=""

DMS_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$DMS_AREA_PATH"

# Do not edit below this line unless very sure ------------------------------------------------------------------------------------------------------------

c3grid_generic
enable_slicestagein
enable_filetransfer
enable_interslicetransfer
enable_mdscatalog
enable_permissions
enable_sliceChown
enable_gsiftpDeadlockPrevention
refresh_system
