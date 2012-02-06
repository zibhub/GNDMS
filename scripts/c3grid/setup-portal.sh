#!/bin/bash

# setup a subspace which can be used between transfers, incase 3rd party transfer isn't applyable

SCRIPTDIR="$(dirname $0)/../" ; source "$SCRIPTDIR"internal/script-setup.sh

GRIDHOST="mardschana2.zib.de"

IMPORT_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
IMPORT_AREA_SIZE="1000000" # Currently unused
IMPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$IMPORT_AREA_PATH"

EXPORT_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
EXPORT_AREA_SIZE="1000000" # Currently unused
EXPORT_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$EXPORT_AREA_PATH"

TRANSFER_AREA_PATH="/NFS3/sesam2_work/work/c3storage"
TRANSFER_AREA_SIZE="1000000" # Currently unused
TRANSFER_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$IMPORT_AREA_PATH"



# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

enable_importSpace
enable_exportSpace
enable_transferSpace

# uncomment these if the transferspace is setup standallone
# additionally make sure that filetransfer and gsi-deadlock prevention are enabled.
#enable_permissions
#enable_sliceChown
#refresh_system
