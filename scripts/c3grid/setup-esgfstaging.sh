#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# %{} is shell variable substitution at container runtime
# $STAGING_COMMAND runs in the slice working dir.

ESGF_STAGING_AREA_PATH="/var/lib/gndms/sub"
ESGF_STAGING_AREA_SIZE="1000000" # Currently unused

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"

STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$STAGING_AREA_PATH"
ESGF_STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$ESGF_STAGING_AREA_PATH"

ESGF_TRUSTSTORE="/etc/grid-security/esgf.truststore"
ESGF_TRUSTSTORE_PASSWORD="esgf.trust"

# default time to live for slices of the Staging slice kind (in milliseconds)
# default: one day
TTL=86400000

UPDATE_INTERVAL=60000


# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_esgfstageing
enable_permissions
enable_sliceChown
refresh_system
