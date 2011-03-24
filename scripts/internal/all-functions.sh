source "$SCRIPTDIR"internal/echo-hostname.sh
source "$SCRIPTDIR"internal/refresh-system.sh
# source "$SCRIPTDIR"internal/create-dms-dir.sh
for i in "$SCRIPTDIR"features/*.sh ; do source $i ; done
