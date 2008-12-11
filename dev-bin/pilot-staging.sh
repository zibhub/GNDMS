#!/bin/bash
#exec $(dirname $0)/ist-staging.sh
#exec $(dirname $0)/../scripts/dummy-staging.sh
#exec $(dirname $0)/dummy-xml-staging.sh

###
# uses this together with short_contract to trigger 
# task killed by its resource
# reduce val 
#val=24
# reduce val to force the task to kill itself
# use it together with task_kill_contract
val=3
exec $(dirname $0)/blocking-staging.sh $val 

#
#sfr="$(cat - | grep -h '^c3grid')"
#
#echo "i'm going to fail"
#echo "and you can't do anything about it"
#
exit 1

