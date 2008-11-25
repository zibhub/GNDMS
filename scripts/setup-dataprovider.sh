#!/bin/sh

SCRIPTDIR="$(dirname $0)/" ; source "$SCRIPTDIR"internal/script-setup.sh

# %{} is shell variable substitution at container runtime

STAGING_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-staging.sh"
ESTIMATION_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-estimation.sh"

STAGING_AREA_PATH="/tmp"
STAGING_AREA_SIZE="1000000" # Currently unused

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"



# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_providerstagein
refresh_system
