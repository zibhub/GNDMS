#!/bin/sh

sfr="$(cat - | grep -h '^c3grid')"

datafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetBaseDataFile | cut -d= -f2-)
metafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetMetaDataFile | cut -d= -f2-)

echo "$sfr" > $datafile
echo "$sfr" > $metafile


exit 0
