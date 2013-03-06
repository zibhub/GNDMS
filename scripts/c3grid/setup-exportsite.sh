#!/bin/bash

# Setup for an export site

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"

# Parameters for the export staging area
EXPORT_AREA_PATH="/var/lib/gndms/sub"
EXPORT_AREA_SIZE="1000000" # Currently unused
EXPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$TRANSFER_AREA_PATH"

# Set a short, human-readable name for the export site to be registered at VolD
EXPORT_NAME=""

# Default time to live for slices of the export staging slice kind (in milliseconds)
# default: one day
TTL=86400000

# VolD update interval in milliseconds
UPDATE_INTERVAL=60000

# Do not edit below this line unless very sure 
# ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_exportSpace

# uncomment these if the export space is setup stand alone
# additionally make sure that filetransfer and gsi-deadlock prevention are enabled.
#enable_permissions
#enable_sliceChown
#refresh_system
