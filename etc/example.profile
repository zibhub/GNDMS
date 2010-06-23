# This has been written for bash

export ANT_HOME=""
export GLOBUS_LOCATION="/opt/gt-4.0.7"
export DERBY_HOME=""
export GROOVY_HOME=""
export GNDMS_SOURCE=""

export GNDMS_SHARED="$GLOBUS_LOCATION/etc/GNDMS_shared"
export GNDMS_MONI_CONFIG="$GNDMS_SHARED/monitor.properties"

export PATH="$PATH:$ANT_HOME/bin:$DERBY_HOME/bin:$GROOVY_HOME/bin:$GNDMS_SOURCE/bin"

source $GLOBUS_LOCATION/etc/globus-user-env.sh
source $GLOBUS_LOCATION/etc/globus-devel-env.sh

