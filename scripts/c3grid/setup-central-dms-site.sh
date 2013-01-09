#!/bin/bash

# Setup for the central DMS

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"

# Set the polling delay for task status queries
POLLING_INTERVAL=2000
UPDATE_INTERVAL=60000


# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_dmsstaging
enable_permissions
enable_sliceChown
refresh_system
