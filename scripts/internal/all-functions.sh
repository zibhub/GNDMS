source "$SCRIPTDIR"internal/echo-hostname.sh
source "$SCRIPTDIR"internal/setup-generic.sh
source "$SCRIPTDIR"internal/check.sh
for i in "$SCRIPTDIR"features/*.sh ; do source $i ; done
