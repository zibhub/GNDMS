#!/bin/bash

GRID_MAPFILE="/etc/grid-security/grid-mapfile"
CHOWN=$( which chown )

failWith() {
    ec=$1
    shift
    msg=$1

    echo $msg
    exit $ec 
}

if [ "$#" -ne "3" ]; then 
    failWith 1 "Wrong number of arguments, expected 3 received $#"
fi

uid=$1
shift
baseDir=$1
shift
sliceId=$1

sliceDir="$baseDir/$sliceId" 

if [ ! -d "$sliceDir" ]; then 
    failWith 2 "Folder $sliceDir doesn't exist"
fi

grep $GRID_MAPFILE -e "\b$uid\b" 2>&1 > /dev/null
if [ "$?" -ne "0" ]; then
    failWith 2 "$uid not in $GRID_MAPFILE"
fi

$CHOWN -R $uid $sliceDir
