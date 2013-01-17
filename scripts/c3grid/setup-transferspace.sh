#!/bin/bash

# Setup for a transfer space which can be used between transfers, in case 3rd party transfer is not applicable

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"
GRIDHOST="mardschana2.zib.de"

# Parameters for the transfer staging area
TRANSFER_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
TRANSFER_AREA_SIZE="1000000" # Currently unused
TRANSFER_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$TRANSFER_AREA_PATH"

# Set a short, human-readable name for the transfer space to be registered at VolD
TRANSFER_NAME=""

# Default time to live for slices of the transfer staging slice kind (in milliseconds)
# default: one day
TTL=86400000

# VolD update interval in milliseconds
UPDATE_INTERVAL="60000"

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_transferSpace

# uncomment these if the transferspace is setup standallone
# additionally make sure that filetransfer and gsi-deadlock prevention are enabled.
#enable_permissions
#enable_sliceChown
#refresh_system
