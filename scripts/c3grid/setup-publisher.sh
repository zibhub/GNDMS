#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

PUBLISHING_AREA_PATH="/var/tmp/gndms/sub"
PUBLISHING_AREA_SIZE="1000000" # Currently unused

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

PUBLISHING_OIDPREFIX="publish1"
# VolD update interval in milliseconds
PUBLISHING_UPDATE_INTERVAL="5000"

# default time to live for slices of the Staging slice kind (in milliseconds)
# default: one day
TTL=86400000


# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_publishing
enable_permissions
enable_sliceChown
refresh_system
