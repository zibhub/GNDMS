#!/bin/bash

#
# Creates the file list for the buildr auto-clean target
#


if [ "$#" -ne "1"  ]; then
    echo "usage: $(basename $0) <version>"
    exit 1
fi 

VERS="$1"
TARGET="$GNDMS_SOURCE/buildr/$VERS"
TARGET_FILE="files"
TEMPLATE="$GNDMS_SOURCE/buildr/gtfresh/files"

source $(dirname $0)/util.sh

gndmsVarCheck

if [ -e "$TARGET" ]; then
    if [ -d "$TARGET" ]; then
        if [ -e "$TARGET/$TARGET_FILE" ]; then
            echo "Destination file"
            echo "  $TARGET/$TARGET_FILE"
            echo "already exists. Please remove it and try again."
            exit 2
        fi
    else 
        echo "Destination "
        echo "  $TARGET"
        echo "already exists and is not a drictory."
        echo "Please remove it and try again."
        echo "Remove it and try again."
        exit 3
    fi
else
    mkdir -p $TARGET
fi

# creates a diff between the current jars in $GLOBUS_LOCATION/lib folder and the reference file gt_lib_clean
ls $GLOBUS_LOCATION/lib/*.jar | cut -d /  -f 4,5  - | xargs -Ifoo echo "/foo" | sort \
    | diff -u $TEMPLATE - | grep '^+/' | tr --delete + \
    | xargs -Ifoo echo "#{ENV['GLOBUS_LOCATION']}"foo \
    > $TARGET/$TARGET_FILE

echo "Wrote diff to $TARGET/$TARGET_FILE"
