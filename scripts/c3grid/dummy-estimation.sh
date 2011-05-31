#!/bin/sh

if [ -e "/tmp/fooasdf" ]; then
    echo "OH NOOZ"
    #exit 1
else 
    touch "/tmp/fooasdf"
fi

cat - 

# exit 255 if unfulfillable
# exit 254 if permission denied for DN

exit 0
