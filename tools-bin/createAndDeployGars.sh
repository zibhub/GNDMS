#!/bin/bash
DEPLOY_CMD="ssh globus@localhost globus-deploy-gar"

source $(dirname $0)/var-check.sh
var_check

cd $C3GRID_SOURCE/services
service_dirs=$( find * -maxdepth 0 -type d )
for i in $service_dirs; do
    if  [ "$#" -eq "1" -a "$1" != "$i" ]; then
        echo "Skipping $i"
        continue
    fi

    if [ "$i" == "WHORFX" ]; then 
        echo "Skipping $i cause it ain't working"
        continue
    fi

    echo "building gar for $i"
    (cd $i && ant createDeploymentGar && $DEPLOY_CMD $(pwd)/c3grid_$i.gar )
done


