#!/bin/sh
PREFIX="$1"
shift
egrep -l -x '^[^#]*c3grid\.StageFileRequest\.ObjectList\.Item\.[0-9]+[:white:]*\='$PREFIX'.*$' $*
