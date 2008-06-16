#!/bin/bash
TARGET="$PWD/types"
for SERVICE in services/* ; do
  rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  rsync -aurl "$SERVICE/schema/" "$TARGET/"
done
for SERVICE in services/* ; do
  for jar in extra/lib/* ; do
    ln -sf "../../../$jar" "$SERVICE/lib" 
  done
  for jar in extra/tools-lib/* ; do
    ln -sf "../../../../$jar" "$SERVICE/tools/lib" 
  done
done
