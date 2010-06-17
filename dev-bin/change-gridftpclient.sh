#!/bin/bash
MODE="UPDATE"

f_dly=
f_to=
f_p=

usage() {

    echo "usage: $(basename $0) [options]"
    echo "Options:"
    echo "-d <int>  the new delay"
    echo "-t <int>  the new timeout"
    echo "-p        only echos the update command"
    echo ""
    exit $1
}

while getopts hpd:t: OPTS;  do
    case $OPTS in
        h) usage 0;;
        d) f_dly=1
           v_dly="delay: ${OPTARG};";;
        t) f_to=1
           v_to="timeout: ${OPTARG};";;
        p) f_p=1;;
        \?)  usage 1;;
        *)   echo "something terrible just happened ...\"$OPTS\"... " >&2
             usage 1;;
    esac
done

if [ -z "$f_dly" ] && [ -z "$f_to" ]; then
    echo "Nothing to do. Please give at least on option."
    usage 0
fi

cmd="moni call -v .sys.SetupDefaultConfiglet \
  mode: $MODE; \
  name: nbcf; \
  className: de.zib.gndms.kit.network.NonblockingClientFactoryConfiglet; \
  $v_dly\
  $v_to"
echo $cmd
if [ -z "$f_p" ]; then
    $cmd
    if [ "$?" -eq "0" ]; then
        echo "Update successful refreshing system"
        moni call .sys.RefreshSystem
        echo
        echo "Done"
    fi
fi
