source internal/all-functions.sh

check_mode
[ ! -z "$GRIDHOST" ] || GRIDHOST=`echo_hostname`
setup_generic

