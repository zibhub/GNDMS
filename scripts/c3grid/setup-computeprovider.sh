#!/bin/bash

# Setup for a compute provider

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

# Set a short, human-readable name for the compute provider to be registered at VolD
CP_NAME=""

# Parameters for the transfer staging area
TRANSFER_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
TRANSFER_AREA_SIZE="1000000" # Currently unused
TRANSFER_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$IMPORT_AREA_PATH"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

# VolD update interval in milliseconds
UPDATE_INTERVAL="60000"

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_computeprovider
enable_permissions
enable_sliceChown
enable_filetransfer()
enable_gsiftpDeadlockPrevention
#refresh_system
