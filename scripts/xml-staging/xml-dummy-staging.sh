#!/bin/bash

# This doesnt work on BSD and MAC out of the box due to an
# incompatible "expr" tool
#
# Only works on Linux!


sfr="$(cat - )"

extFn() {
    idx1=$( expr index $1 '>' )
    let idx1++
    sstr=$( expr substr $1 $idx1 $( expr length $1 ) )
    len=$( expr index $sstr '<' )
    let len--

    expr substr $1 $idx1 $len
}


datafile=$(echo "$sfr" | grep '\bDataFile\b' )
datafile=$( extFn $datafile )
if [ -z $datafile ]; then
    echo "Can't extract data file name from:"
    echo $sfr
    exit 1
fi
metafile=$(echo "$sfr" | grep '\bMetadataFile\b' )
metafile=$( extFn $metafile )
if [ -z $metafile ]; then
    echo "Can't extract meta file name from:"
    echo $sfr
    exit 1
fi

echo "$sfr" > $datafile
echo "$sfr" > $metafile


exit 0
