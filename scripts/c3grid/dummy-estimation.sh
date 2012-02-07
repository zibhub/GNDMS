#!/bin/sh

# uncomment the following lines, to simulate estimation script failure
#LOCKFILE="/tmp/unestimatable" 
#if [ -e "$LOCKFILE" ]; then
#    echo "OH NOOZ"
#    exit 1
#else 
#    touch $LOCKFILE
#fi

#cat - 

while read LINE; do
    [ "${LINE}" != "${LINE#c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil=}" ] && HAVE_LIKELY=true
    [ "${LINE}" != "${LINE#c3grid.StageFileRequest.Estimate.ResultValidUntil=}" ] && HAVE_VALID=true
    [ "${LINE}" != "${LINE#c3grid.StageFileRequest.Estimate.MaxSize=}" ] && HAVE_MAXSIZE=true
    echo "${LINE}"
done

[ -z "${HAVE_LIKELY}" ] && echo "c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil=5000"
[ -z "${HAVE_VALID}" ] && echo "c3grid.StageFileRequest.Estimate.ResultValidUntil=600000"
[ -z "${HAVE_MAXSIZE}" ] && echo "c3grid.StageFileRequest.Estimate.MaxSize=123456"

# exit 255 if unfulfillable
# exit 254 if permission denied for DN

exit 0
