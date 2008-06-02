#!/bin/bash
TARGET="$PWD/types"
for SERVICE in services/* ; do
  rsync -a "$SERVICE/build/schema/" "$TARGET/"
  rsync -a "$SERVICE/schema/" "$TARGET/"
done
