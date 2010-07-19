#!/bin/sh

export SCRIPTDIR="$(dirname $0)/../"

source "$SCRIPTDIR"internal/script-setup.sh

# chown globus:globus $STAGING_AREA_PATH
# chmod +t $STAGING_AREA_PATH
# chmod a+w $STAGING_AREA_PATH
STAGING_AREA_PATH="/tmp/gndms"

# Set your hostname if it is not detected correctly
# GRIDHOST=""

# URL to GridFTP Space
STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"

if [ "$MODE" == "CREATE" ]; then
    createDMSDir $STAGING_AREA_PATH
fi

# Do not edit below this line unless very sure ------------------------------------------------------------------------

ptgrid_generic
enable_ptresource
enable_filetransfer
enable_interslicetransfer
enable_permissions
enable_gsiftpDeadlockPrevention
refresh_system
