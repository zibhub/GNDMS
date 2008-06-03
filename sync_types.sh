#!/bin/bash
TARGET="$PWD/types"
for SERVICE in services/* ; do
  rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  rsync -aurl "$SERVICE/schema/" "$TARGET/"
done
