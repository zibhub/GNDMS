# Setup for the portal

#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"
GRIDHOST="mardschana2.zib.de"

# Set a short, human-readable name for the portal
PORTAL_NAME=""

# Parameters for the import staging area
IMPORT_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
IMPORT_AREA_SIZE="1000000" # Currently unused
IMPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$IMPORT_AREA_PATH"

# Set a short, human-readable name for the import space to be registered at VolD
# If this is not explicitly set, the general PORTAL_NAME is used
IMPORT_NAME="$PORTAL_NAME"

# Parameters for the export staging area
EXPORT_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
EXPORT_AREA_SIZE="1000000" # Currently unused
EXPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$EXPORT_AREA_PATH"

# Set a short, human-readable name for the export space to be registered at VolD
# If this is not explicitly set, the general PORTAL_NAME is used
EXPORT_NAME="$PORTAL_NAME"

# Parameters for the transfer staging area
TRANSFER_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
TRANSFER_AREA_SIZE="1000000" # Currently unused
TRANSFER_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$IMPORT_AREA_PATH"

# Set a short, human-readable name for the transfer space to be registered at VolD
# If this is not explicitly set, the general PORTAL_NAME is used
TRANSFER_NAME="$PORTAL_NAME"

# VolD update interval in milliseconds
UPDATE_INTERVAL="60000"

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_importSpace
enable_exportSpace
enable_transferSpace
enable_permissions
enable_sliceChown
enable_filetransfer()
enable_gsiftpDeadlockPrevention
#refresh_system
