# Setup for an import site

#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"
GRIDHOST="mardschana2.zib.de"

# Parameters for the import staging area
IMPORT_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
IMPORT_AREA_SIZE="1000000" # Currently unused
IMPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$TRANSFER_AREA_PATH"

# Set a short, human-readable name for the import site to be registered at VolD
IMPORT_NAME=""

# Default time to live for slices of the import staging slice kind (in milliseconds)
# default: one day
TTL=86400000

# VolD update interval in milliseconds
UPDATE_INTERVAL=60000

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_importSpace

# uncomment these if the import space is setup standal lone
# additionally make sure that filetransfer and gsi-deadlock prevention are enabled.
#enable_permissions
#enable_sliceChown
#refresh_system