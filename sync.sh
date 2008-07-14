#!/bin/bash
TARGET="$PWD/types"
for SERVICE in services/* ; do
  rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  rsync -aurl "$SERVICE/schema/" "$TARGET/"
done
for SERVICE in services/* ; do
  for gar in "$SERVICE"/*.gar ; do
    echo ln -sf "$gar" .
    ln -sf "$gar" .
  done
  for jar in extra/lib/* ; do
    echo ln -sf "../../../$jar" "$SERVICE/lib" 
    ln -sf "../../../$jar" "$SERVICE/lib" 
  done
  for jar in extra/tools-lib/* ; do
    echo ln -sf "../../../../$jar" "$SERVICE/lib" 
    ln -sf "../../../$jar" "$SERVICE/lib" 
  done
done
( cd services/DSpace && ln -sf "../../../model/lib/gndms-dspace-model.jar" lib/ )
( cd services/LOFIS && ln -sf "../../../model/lib/gndms-lofis-model.jar" lib/ )
( cd services/GORFX && ln -sf "../../../model/lib/gndms-gorfx-model.jar" lib/ )
