#!/bin/bash

varcheck() {
    eval v=\$$1
    if [ "x$v" == "x" ]; then
        echo "Please export $1 and try again!"
        exit 1
    fi 
}

varcheck GNDMS_SOURCE
varcheck GLOBUS_LOCATION


clap=$($GNDMS_SOURCE/bin/gndms-cp)

exec java -Daxis.ClientConfigFile=$GLOBUS_LOCATION/client-config.wsdd \
    -cp $clap de.zib.gndmc.SliceInOutClient \
    -p $GNDMS_SOURCE/etc/sliceInOutClient.properties 
