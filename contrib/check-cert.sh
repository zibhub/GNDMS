#!/bin/bash

# checks if a user cert is accepted by a given host
# default mardschana2.zib.de:8443

HOST="mardschana2.zib.de"
PORT="8443"
CERTDIR="/etc/grid-security/certificates"

fail() {
    ev=0
    if [ -n "$1" ]; then
        echo $(basename $0): $1
        echo
        ev=1
    fi
    echo "Usage: $(basename $0) [Options]"
    echo "Description: "
    echo "Checks if a user cert is accepted by a given host. (Default mardschana2.zib.de:8443)"
    echo "If the cert is accepted a session is opened which should be closed using CTRL-C. "
    echo "Otherwise the server refuses the connection."
    echo "Options:"
    echo "  -u <host> The host-url (default: $HOST)"
    echo "  -p <int>  The destination (default: $PORT)"
    echo "  -c <dir>  The local certificate directory (default: $CERTDIR)"
    echo "  -h        Show this help."
    exit $ev
}

while [ "$#" -gt 0 ]; do
    if [ "$1" = "-u" ]; then
        shift
        HOST=$1
    elif [ "$1" = "-p" ]; then
        shift
        if [ `expr match "$1" '[0-9]\{1,\}$'` -gt 0 ]; then
            PORT=$1
        else 
            fail "-p expects an integer argument > 0"
        fi
    elif [ "$1" = "-c" ]; then 
        shift
        if [ -d $1 ]; then
            CERTDIR=$1
        else 
            fail "-d expects an existing directory"
        fi
    else 
        fail "Unknown parameter: $1"
    fi
    shift
done

openssl s_client -ssl3 -cert ~/.globus/usercert.pem -key ~/.globus/userkey.pem \
-CApath $CERTDIR -connect $HOST:$PORT


# vim:tw=0
