#!/bin/bash
#exec $(dirname $0)/ist-staging.sh
#exec $(dirname $0)/blocking-staging.sh


sfr="$(cat - | grep -h '^c3grid')"



echo "i'm going to fail"
echo "and you can't do anything about it"



exit 1
