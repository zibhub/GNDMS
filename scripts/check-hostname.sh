
if which hostname &> /dev/null; then
    hn=$( hostname -f )
fi

if [ -z $hn ]; then
    if [ -z $HOST ]; then 
        hn=$HOST
    else 
        hn=$HOSTNAME
    fi
fi

# uncomment the following line to set your hostname manually
#hn=<hostname>

if [ -z $hn ]; then 
    echo "unable to aquire hostname, please set it manually"
    exit 1
fi

