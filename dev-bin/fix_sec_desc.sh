#!/bin/bash
source $(dirname $0)/var-check.sh

var_check

XSLT_FILE=$(dirname $0)/fix_sec_desc.xsl

run_saxon() {

    java -jar $C3GRID_SOURCE/extra/tools-lib/saxon9.jar -s:$1 \
    -xsl:$XSLT_FILE -o:$2
}


for i in $C3GRID_SOURCE/services/*/etc/*-security-desc.xml; do
    echo $i
    run_saxon $i $i
done
