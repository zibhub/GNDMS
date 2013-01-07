# Setup for an ESGF staging site

#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Parameters for the ESGF staging area
ESGF_STAGING_AREA_PATH="/var/lib/gndms/sub"
ESGF_STAGING_AREA_SIZE="5000000000" # 5GB

# Set a short, human-readable name for the ESGF staging site to be registered at VolD
ESGF_STAGING_NAME=""

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"

# GridFTP parameter
ESGF_STAGING_AREA_GSI_FTP_URL="gsiftp://$GRIDHOST""$ESGF_STAGING_AREA_PATH"

# Truststore parameters
ESGF_TRUSTSTORE="/etc/grid-security/esgf.truststore"
ESGF_TRUSTSTORE_PASSWORD="esgf.trust"

# Default time to live for slices of the ESGF Staging slice kind (in milliseconds)
# default: one day
TTL=86400000

# VolD update interval in milliseconds
UPDATE_INTERVAL=60000

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_esgfstageing
enable_permissions
enable_sliceChown
refresh_system
