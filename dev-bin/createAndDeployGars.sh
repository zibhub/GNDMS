#!/bin/bash
DEPLOY_CMD="ssh globus@localhost globus-deploy-gar"

source $(dirname $0)/var-check.sh
var_check

list_contain() {

    x=$1
    shift

    for y in $@; do
        if [ "$y" = "$x" ]; then 
            return 0
        fi
    done
    return 1
}

cd $GNDMS_SOURCE/services
service_dirs=$( find * -maxdepth 0 -type d )
for i in $service_dirs; do
    if  [ "$#" -ge "1" ]; then 
        list_contain $i $@
        if [ "$?" -eq "1" ] ; then
            echo "Skipping $i"
            continue
        fi
    fi

    if [ "$i" == "WHORFX" ]; then 
        echo "Skipping $i cause it ain't working"
        continue
    fi

    echo "building gar for $i"
    (cd $i && ant createDeploymentGar && $DEPLOY_CMD $(pwd)/gndms_$i.gar )
    if [ "$?" -ne "0" ]; then
        exit 1
    fi
done
echo "done at $(date)"


