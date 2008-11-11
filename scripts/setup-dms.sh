#!/bin/sh

# %{} is shell variable substitution at container runtime

MDS_URL="http://c3grid-gt.e-technik.uni-dortmund.de:8080/webmds/webmds?info=indexinfo"


# Do not edit below this line unless very sure ------------------------------------------------------------------------------------------------------------

MODE=$1

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE


# Setup generic entities

env GNDMS_SCRIPT=1 `dirname $0`/setup-generic.sh $MODE

moni call -v .sys.SetupDefaultConfiglet "mode: '$MODE'; name: 'mds'; className: 'de.zib.gndms.infra.configlet.C3MDSConfiglet'; \
  delay: '30000'; initialDelay: '2000'; mdsUrl: '$MDS_URL'"

moni call -v .sys.RefreshSystem
