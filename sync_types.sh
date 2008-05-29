#!/bin/bash
TARGET="$PWD/types/"
for SERVICE in services/* ; do
  ( cd "$SERVICE/schema" && cp -fR `basename "$SERVICE"` $TARGET )
done
