source "$SCRIPTDIR"internal/echo-hostname.sh
source "$SCRIPTDIR"internal/refresh-system.sh
source "$SCRIPTDIR"internal/setup-generic.sh
for i in "$SCRIPTDIR"features/*.sh ; do source $i ; done
