#!/bin/sh

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

STAGING_AREA_PATH="/tmp"
STAGING_AREA_SIZE="1000000" # Currently unused

# Set your hostname if it is not detected correctly
# GRIDHOST=""

STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"



# Do not edit below this line unless very sure ------------------------------------------------------------------------

enable_ptresource
enable_filetransfer
enable_interslicetransfer
enable_permissions
refresh_system
