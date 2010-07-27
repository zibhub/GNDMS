#!/bin/bash
source $(dirname $0)/var-check.sh

var_check

if [ "$#" -eq "1" ]; then 
    SERVICE_NAME="$1"
else 
    SERVICE_NAME="*"
fi

XSLT_FILE=$(dirname $0)/fix_sec_desc.xsl

run_saxon() {

    java -jar $GNDMS_SOURCE/extra/saxon9.jar -s:$1 \
    -xsl:$XSLT_FILE -o:$2
}


for i in $GNDMS_SOURCE/services/$SERVICE_NAME/etc/*-security-desc.xml; do
    echo $i
    run_saxon $i $i
done
