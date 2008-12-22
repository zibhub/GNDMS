#!/bin/sh

cat - > /dev/null

echo c3grid.StageFileRequest.Estimate.MaxSize=26753248
echo c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil=425000
echo c3grid.StageFileRequest.Estimate.RequestInfo=BlahBlahBl

# exit 255 if unfulfillable
# exit 254 if permission denied for DN

exit 0
???
