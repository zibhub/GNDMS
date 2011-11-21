#!/bin/sh

# uncomment the following lines, to simulate estimation script failure
#LOCKFILE="/tmp/unestimatable" 
#if [ -e "$LOCKFILE" ]; then
#    echo "OH NOOZ"
#    exit 1
#else 
#    touch $LOCKFILE
#fi

cat - 

# exit 255 if unfulfillable
# exit 254 if permission denied for DN

exit 0
