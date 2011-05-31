#!/bin/sh

echo "eaeoeiuieeueeoeiiieoeioeoe" > .GARBAGE

sfr="$(cat - | grep -h '^c3grid')"

datafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetBaseDataFile | cut -d= -f2-)
metafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetMetaDataFile | cut -d= -f2-)


# fake execution: seconds to wait
sleep 5


echo "$sfr" > $datafile
echo "$sfr" > $metafile


# Uncomment to test dummy-cancel.sh
#echo "NAY!" >&2
#exit 1

# example of proxy usage

# use something like to test new cert delegation
# the cert can be directly accessed trough X509_USER_PROXY
#globus-url-copy file://$(pwd)/$datafile gsiftp://mardschana2.zib.de/tmp/$datafile
#
#if [ "$?" -ne "0" ]; then
#    exit 1
#fi


rm -f .GARBAGE

echo "YAY!"

exit 0
