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

if [ "$#" -ne "1" ]; then
	echo "usage $(basename $0 ) <slice-io-properties>"
	exit 1
fi

clap=$($GNDMS_SOURCE/bin/gndms-cp)

exec java -Daxis.ClientConfigFile=$GLOBUS_LOCATION/client-config.wsdd \
    -cp $clap de.zib.gndmc.SliceInOutClient \
    -p $1
