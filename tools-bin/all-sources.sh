source $(dirname $0)/var-check.sh
var_check

find $C3GRID_SOURCE/typecon/src -type f
find $C3GRID_SOURCE/typecon/test-src -type f
find $C3GRID_SOURCE/logic/src -type f
find $C3GRID_SOURCE/logic/test-src -type f
find $C3GRID_SOURCE/model/src -type f
find $C3GRID_SOURCE/model/test-src -type f
find $C3GRID_SOURCE/infra/src -type f
find $C3GRID_SOURCE/infra/test-src -type f
find $C3GRID_SOURCE/kit/src -type f
find $C3GRID_SOURCE/kit/test-src -type f
find $C3GRID_SOURCE/services/*/test/src -type f | grep -v README.txt
echo $C3GRID_SOURCE/services/WHORFX/src/de/zib/gndms/service/WHORFXImpl.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/DSpaceResource.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/ExtDSpaceResourceHome.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/DSpaceImpl.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/SubspaceResource.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/ExtSubspaceResourceHome.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/SubspaceImpl.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/SliceResource.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/ExtSliceResourceHome.java
echo $C3GRID_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/SliceImpl.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/GORFXResource.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/ExtGORFXResourceHome.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/GORFXImpl.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ORQResource.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ExtORQResourceHome.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/ORQImpl.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/OfferResource.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/ExtOfferResourceHome.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/OfferImpl.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/TaskImpl.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/TaskResource.java
echo $C3GRID_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/ExtTaskResourceHome.java
