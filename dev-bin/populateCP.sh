

clap=""
addToCP() { 
for i in $( find $1 -maxdepth 1 -name '*.jar' ); do
    clap=$i:$clap
done
}

#addToCP $GNDMS_SOURCE/extra/tools-lib
addToCP $GLOBUS_LOCATION/lib
for i in $( find $GNDMS_SOURCE/lib -maxdepth 1 -type d ); do
    addToCP $i
done

echo $clap
