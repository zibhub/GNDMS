MODE="$1"
shift

if [ "$MODE" = "-addon" ] ; then
	ADDON=true
	MODE="$1"
	shift
fi

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE

source "$SCRIPTDIR"internal/all-functions.sh

[ ! -z "$GRIDHOST" ] || GRIDHOST=`echo_hostname`

