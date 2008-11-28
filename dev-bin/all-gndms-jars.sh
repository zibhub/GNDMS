#!/bin/sh
source $(dirname $0)/var-check.sh
var_check

find $C3GRID_SOURCE -type f -name 'c3grid*.jar'
