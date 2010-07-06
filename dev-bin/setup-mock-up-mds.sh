#!/bin/sh

SCRIPTDIR="$(dirname $0)/" ; source "$SCRIPTDIR"internal/script-setup.sh

MODUS="CREATE"
MDS_URL="$GNDMS_SOURCE/test-data/webmds.xml"
MDS_PREFIX="g2."

enable_mockup_mdscatalog
