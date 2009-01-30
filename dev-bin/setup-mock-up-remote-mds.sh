#!/bin/sh

SCRIPTDIR="$(dirname $0)/" ; source "$SCRIPTDIR"features/enable-mdscatalog.sh

MODE="CREATE"
MDS_URL="http://csr-priv5:7403/~mjorra/test-data/webmds.xml"
MDS_PREFIX="g2."

enable_mdscatalog
