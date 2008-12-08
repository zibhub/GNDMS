#!/bin/sh

# generate 5 to 10 files of arbitrary size between 1 kB and 1 MB
source $(dirname $0)/blocking-staging.sh

DATA_SRC_DIR=
sfr="$(cat - | grep -h '^c3grid')"

datafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetBaseDataFile | cut -d= -f2-)
metafile=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetMetaDataFile | cut -d= -f2-)
dataext=$(echo "$sfr" | grep c3grid.StageFileRequest.TargetFileFormat | cut -d= -f2-)
database=$(basename $datafile .$dataext)


RANGE=10
FLOOR=5
MIN_FILESIZE=1
MAX_FILESIZE=1024

# $1 = floor $2 = range
rand_between () {

    if [ "$#" -ne "2" ]; then
        echo "two paramters are required"
        exit 1
    fi

    val=0
    while [ "$val" -le "$1" ]; do
        val=$RANDOM
        let "val %= $2"
    done

    return $val
}

rand_between $FLOOR $RANGE

cnt=$?

echo "Filecount: $cnt" > $metafile

for i in $(seq $cnt); do

    rand_between $MIN_FILESIZE $MAX_FILESIZE
    fs=$?
    fn=${database}_$i.$dataext
    dd if=/dev/urandom of=$fn bs=1kB count=$fs
    echo "$(basename $fn) $fs" >> $metafile
done 

md5sum * > md5sums

exit 0
