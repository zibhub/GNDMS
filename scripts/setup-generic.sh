#!/bin/sh

[ -z "$GNDMS_SCRIPT" ] && echo "Dont call us; we call you." && exit 1

MODE=$1

moni call .dspace.SetupSliceKind "uri:'http://www.c3grid.de/G2/SliceKind/DMS'; sliceKindMode:RO; mode: "$MODE
