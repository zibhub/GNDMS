# This has been written for bash

# Top-level installation directory of globus toolkit 4.0.8
export GLOBUS_LOCATION="/opt/gt-current"

# Top-level distribution directory of GNDMS software
export GNDMS_SOURCE=""

# export JAVA_HOME=""
# export ANT_HOME=""

export GNDMS_SHARED="$GLOBUS_LOCATION/etc/gndms_shared"
export GNDMS_MONI_CONFIG="$GNDMS_SHARED/monitor.properties"

export PATH="$PATH:$ANT_HOME/bin:$GNDMS_SOURCE/bin"

# You might want to source these
#
# source $GLOBUS_LOCATION/etc/globus-user-env.sh
# source $GLOBUS_LOCATION/etc/globus-devel-env.sh

