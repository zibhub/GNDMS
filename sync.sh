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
  echo ln -sf "../../../model/lib/gndms-shared-model.jar" "$SERVICE/lib/"
  ln -sf "../../../model/lib/gndms-shared-model.jar" "$SERVICE/lib/"
  for jar in "$GROOVY_HOME"/embeddable/groovy-all*jar ; do
  	echo ln -sf "$jar" "$SERVICE/lib/"
	ln -sf "$jar" "$SERVICE/lib/"
  done
done
( cd bin && ln -sf moni_open moni_repl )
( cd bin && ln -sf moni_open moni_script )
( cd bin && ln -sf moni_open moni_batch )
( cd bin && ln -sf moni_open moni_escript )
( cd bin && ln -sf moni_open moni_eval_script )
