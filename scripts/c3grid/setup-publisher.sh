# Setup for a publishing site

#!/bin/bash

SCRIPTDIR="$(dirname $0)/../" ;
source "$SCRIPTDIR"internal/script-setup.sh

# Parameters for the publishing area
PUBLISHING_AREA_PATH="/var/lib/gndms/sub"
PUBLISHING_AREA_SIZE="5000000000" # 5GB

# Set a short, human-readable name for the publishing site to be registered at VolD
PUBLISHING_NAME=""

# In- and Output format for script properties
# currently only PROPS (java-properties) and XML are supported
SCRIPT_IO_FORMAT="PROPERTIES"
#SCRIPT_IO_FORMAT="XML"

# Set your hostname if it is not detected correctly
#GRIDHOST="$(hostname -f)"

# Set the OID prefixes for this publisher
# Note, that multiple prefixes can be given
PUBLISHING_OIDPREFIX="c3-po.zib.de"

# VolD update interval in milliseconds
UPDATE_INTERVAL="60000"

# default time to live for slices of the publishing slice kind (in milliseconds)
# default: one day
TTL=86400000

# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

#c3grid_generic
enable_publishing
enable_permissions
enable_sliceChown
refresh_system
