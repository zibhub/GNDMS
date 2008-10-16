#!/bin/sh
TARGET="$PWD/types"
for SERVICE in services/* ; do
  echo rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  echo rsync -aurl "$SERVICE/schema/" "$TARGET/"
  rsync -aurl "$SERVICE/schema/" "$TARGET/"
done
for SERVICE in services/* ; do
  echo mkdir -p $SERVICE/test/lib
  mkdir -p $SERVICE/test/lib
  echo find $SERVICE -type f -name '*.gar' -exec ln -sf {} $PWD \;
  find $SERVICE -type f -name '*.gar' -exec ln -sf {} $PWD \;
  for jar in extra/lib/* ; do
    echo ln -sf "../../../$jar" "$SERVICE/lib" 
    ln -sf "../../../$jar" "$SERVICE/lib" 
  done
  echo ln -sf "$GROOVY_HOME"/embeddable/groovy-all*jar "extra/tools-lib/gndms-groovy.jar"
  ln -sf "$GROOVY_HOME"/embeddable/groovy-all*jar "extra/tools-lib/gndms-groovy.jar"
  for jar in extra/tools-lib/* ; do
    echo ln -sf "../../../../$jar" "$SERVICE/lib" 
    ln -sf "../../../$jar" "$SERVICE/lib" 
  done
done
( cd bin && ln -sf moni_open moni_repl )
( cd bin && ln -sf moni_open moni_script )
( cd bin && ln -sf moni_open moni_batch )
( cd bin && ln -sf moni_open moni_escript )
( cd bin && ln -sf moni_open moni_eval_script )
