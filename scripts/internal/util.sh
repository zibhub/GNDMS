#!/bin/bash

# some usefull functions for gndms auxilaray scripts.

RELEASE="0.3.2"

varcheck() {

    var="\$$1"
    var2=$( eval "echo $var" )
    if [ "x$var2" == "x" ]; then
        echo "please export $var"
        exit 1
    fi
}

gndmsVarCheck( ) {
    varcheck "GLOBUS_LOCATION"
    varcheck "GNDMS_SOURCE"
}

