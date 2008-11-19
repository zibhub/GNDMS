#!/bin/sh
`dirname $0`/all-sources.sh $* | xargs wc -l
