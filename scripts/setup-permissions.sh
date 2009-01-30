#!/bin/sh
source $(dirname $0)/features/enable-permissions.sh

[ -z "$1" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

MODE="$1"

enable_permissions
