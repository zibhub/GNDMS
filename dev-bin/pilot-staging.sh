#!/bin/bash

# common test-case
# run successfull staging 
# run script with exitcode 1
# use short_contract look in the log for task resource removal

# run long job  with short contract ( gets killed by resource )

# run short job with kill_contract ( task timeouts before resource )
# wait for resouce to kill task

# setup workspace for xml staging 
# run dummy-xml stager script

#exec $(dirname $0)/ist-staging.sh
#exec $(dirname $0)/../scripts/dummy-staging.sh
#exec $(dirname $0)/dummy-xml-staging.sh

###
# uses this together with short_contract to trigger 
# task killed by its resource
# reduce val 
val=24
# reduce val to force the task to kill itself
# use it together with task_kill_contract
# depending on cpu power and container state
# this haven't allway to work
val=3
exec $(dirname $0)/blocking-staging.sh $val 

#
#sfr="$(cat - | grep -h '^c3grid')"
#sleep 2
#
#echo "i'm going to fail"
#echo "and you can't do anything about it"
#
#exit 1

