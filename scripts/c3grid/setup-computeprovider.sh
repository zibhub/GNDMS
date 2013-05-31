#!/bin/bash

# Setup for a compute provider

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

# Set a short, human-readable name for the compute provider to be registered at VolD
SITE_NAME=""

# Parameters for the transfer staging area
TRANSFER_AREA_PATH="/var/lib/gndms/sub"
TRANSFER_AREA_SIZE="5000000000"
TRANSFER_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$TRANSFER_AREA_PATH"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

# VolD update interval in milliseconds
UPDATE_INTERVAL="50000"

# default time to live for slices of the Computing slice kind (in milliseconds)
# default: one day
TTL=86400000

# Do not edit below this line unless very sure 
# If you do not want to have an automatic VolD registration of the CP, please uncomment the line enable_voldregistration
# ---------------------------------------------------------------------------------------------------------------------------------------------------

TYPE="CPID_GRAM"

#c3grid_generic
enable_computeprovider
enable_voldregistration
enable_permissions
enable_sliceChown
#enable_filetransfer()
#enable_gsiftpDeadlockPrevention
#refresh_system
