# This has been written for bash

export ANT_HOME=""
export GLOBUS_LOCATION="/opt/gt-4.0.7"
export DERBY_HOME=""
export GROOVY_HOME=""
export C3GRID_SOURCE=""

export C3GRID_SHARED="$GLOBUS_LOCATION/etc/c3grid_shared"
export GOMI_CONFIG="$C3GRID_SHARED/monitor.properties"

export PATH="$PATH:$ANT_HOME/bin:$DERBY_HOME/bin:$GROOVY_HOME/bin:$C3GRID_SOURCE/bin"

source $GLOBUS_LOCATION/etc/globus-user-env.sh
source $GLOBUS_LOCATION/etc/globus-devel-env.sh

