#!/bin/bash

# Setup for a compute provider

# Set a short, human-readable name for the compute provider to be registered at VolD
CP_NAME=""

# Setup for a data provider

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Set the scripts for estimation and staging
# $STAGING_COMMAND runs in the slice working dir.
STAGING_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-staging.sh"
ESTIMATION_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-estimation.sh"

# If set, $CANCEL_COMMAND is called whenever a staging script fails
# or is aborted prematurely (i.e. due to a timeout of the associated Task Resouce
# or a user abort). 
# $CANCEL_COMMAND runs in the slice working dir.
# Set to "" if you do not have a cancel/cleanup script.
# If set, ensure that $CANCEL_COMMAND always terminates and only runs for a short 
# duration of time. 
CANCEL_COMMAND="%{GNDMS_SOURCE}/scripts/c3grid/dummy-cancel.sh"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

# Parameters for the data provider staging area
STAGING_AREA_PATH="/var/lib/gndms/sub"
STAGING_AREA_SIZE="5000000000" # 5GB
STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"

# Set the OID prefixes for this data provider
# Note, that multiple prefixes can be given
STAGING_OIDPREFIXE="prefix1 prefix2, prefix3;prefix4"

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# VolD update interval in milliseconds
UPDATE_INTERVAL="60000"

# default time to live for slices of the Staging slice kind (in milliseconds)
# default: one day
TTL=86400000

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_computeAndDataprovider
enable_permissions
enable_sliceChown
enable_filetransfer()
enable_gsiftpDeadlockPrevention
refresh_system
