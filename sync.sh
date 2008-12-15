#!/bin/sh
echoEval() {
echo "$@" 
eval "$@"
}

TARGET="$PWD/types"
for SERVICE in services/* ; do
  echoEval rsync -aurl "$SERVICE/build/schema/" "$TARGET/"
  echoEval rsync -aurl "$SERVICE/schema/" "$TARGET/"
done
for SERVICE in services/* ; do
  echoEval mkdir -p $SERVICE/test/lib
  echoEval mkdir -p $SERVICE/test/src
  echoEval ln -sf ../../../../../../typecon/shared_service_src/shared $SERVICE/src/de/zib/gndms
  echoEval find $SERVICE -type f -name \'*.gar\' -exec ln -sf \'{}\' $PWD \\\;
  for jar in extra/tools-lib/* ; do
    echoEval ln -sf "../../../$jar" "$SERVICE/lib" 
  done
  echoEval ln -sf "$GROOVY_HOME"/embeddable/groovy-all*jar "extra/tools-lib/gndms-groovy.jar"
  for jarname in stuff shared-model logic kit typecon infra ; do
    jar="extra/lib/gndms-$jarname.jar"
    echoEval ln -sf "../../../../$jar" "$SERVICE/lib" 
  done
done
# link DSpace.jar to GORFX
jar="extra/lib/DSpace.jar"
SERVICE="services/GORFX"
echo ln -sf "../../../../$jar" "$SERVICE/lib" 
ln -sf "../../../$jar" "$SERVICE/lib" 
( cd bin && ln -sf moni_open moni_repl )
( cd bin && ln -sf moni_open moni_script )
( cd bin && ln -sf moni_open moni_batch )
( cd bin && ln -sf moni_open moni_escript )
( cd bin && ln -sf moni_open moni_eval_script )
