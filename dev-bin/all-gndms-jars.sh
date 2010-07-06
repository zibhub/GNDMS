#!/bin/sh
source $(dirname $0)/var-check.sh
var_check

find $GNDMS_SOURCE -type f -name 'c3grid*.jar'
