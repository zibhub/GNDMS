echo_hostname() {
if hostname &> /dev/null; then
    if hostname -f &> /dev/null; then
        # Linux
    	hn=$( hostname -f )
    else
       # BSD and Mac
        hn=$( hostname )
    fi
fi

if [ -z "$hn" ]; then
    if [ ! -z "$HOST" ]; then 
        hn="$HOST"
    else 
        hn="$HOSTNAME"
    fi
fi

# uncomment the following line to set your hostname manually
#hn=<hostname>

if [ -z "$hn" ]; then 
    echo "Unable to aquire hostname, please set it manually"
    exit 1
else
    echo "$hn"
fi

}


