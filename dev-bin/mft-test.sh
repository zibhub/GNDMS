
if [ "$#" -ne "1" ]; then
    echo "usage: $(basname $1) multi-request-props"
    exit 1;
fi

BUILDR="buildr"
#gndms-buildr 

echo "running with props: $1"

export GNDMS_PROPS=$1
exec $BUILDR gndms:gndmc:run-stress-test
