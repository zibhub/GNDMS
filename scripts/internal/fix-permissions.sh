#!/bin/bash

echo "Correcting file permissions below $GNDMS_SHARED"
chmod og-rwx "$GNDMS_SHARED/." && chmod og-rwx "$GNDMS_SHARED/monitor.properties" && chmod -R og-rwx "$GNDMS_SHARED/db"
chmod -R og-rwx "$GNDMS_SHARED/log" && ( [ -d "$GNDMS_SHARED/neo" ] && chmod -R og-rwx "$GNDMS_SHARED/neo" )
