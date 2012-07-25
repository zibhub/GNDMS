#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

# %{} is shell variable substitution at container runtime
# $STAGING_COMMAND runs in the slice working dir.

STAGING_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-staging.sh"
ESTIMATION_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-estimation.sh"

# If set, $CANCEL_COMMAND is called whenever a staging script fails
# or is aborted prematurely (i.e. due to a timeout of the associated Task Resouce
# or a user abort). $CANCEL_COMMAND runs in the slice working dir.
#
# Set to "" if you dont have a cancel/cleanup script.  If you set this, 
# make sure that $CANCEL_COMMAND always terminates and only runs for a short 
# duration of time. 
#
CANCEL_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-cancel.sh"

STAGING_AREA_PATH="/var/tmp/gndms/sub"
STAGING_AREA_SIZE="1000000" # Currently unused

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"
STAGING_OIDPREFIXE="prefix1 prefix2, prefix3;prefix4"
# VolD update interval in milliseconds
STAGING_UPDATE_INTERVAL="5000"

# default time to live for slices of the Staging slice kind (in milliseconds)
# default: one day
TTL=86400000


# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_providerstagein
enable_permissions
enable_sliceChown
refresh_system
