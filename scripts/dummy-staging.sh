#!/bin/sh

SFR=`cat - | grep -v '^#'` 

DATAFILE=`echo $SFR | grep c3grid.StageFileRequest.TargetBaseDataFile | cut -d= -f2-`
METAFILE=`echo $SFR | grep c3grid.StageFileRequest.TargetMetaDataFile | cut -d= -f2-`

echo $SFR > $DATAFILE
echo $SFR > Â$METAFILE

exit 0
