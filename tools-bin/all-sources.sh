find logic/src -type f
find logic/test-src -type f
find model/src -type f
find model/test-src -type f
find infra/src -type f
find infra/test-src -type f
find kit/src -type f
find kit/test-src -type f
find services/*/test/src -type f | grep -v README.txt
echo services/WHORFX/src/de/zib/gndms/service/WHORFXImpl.java
echo services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/DSpaceResource.java
echo services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/ExtDSpaceResourceHome.java
echo services/DSpace/src/de/zib/gndms/dspace/service/DSpaceImpl.java
echo services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/SubspaceResource.java
echo services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/ExtSubspaceResourceHome.java
echo services/DSpace/src/de/zib/gndms/dspace/subspace/service/SubspaceImpl.java
echo services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/SliceResource.java
echo services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/ExtSliceResourceHome.java
echo services/DSpace/src/de/zib/gndms/dspace/slice/service/SliceImpl.java
echo services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/GORFXResource.java
echo services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/ExtGORFXResourceHome.java
echo services/GORFX/src/de/zib/gndms/GORFX/service/GORFXImpl.java
echo services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ORQResource.java
echo services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ExtORQResourceHome.java
echo services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/ORQImpl.java
echo services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/OfferResource.java
echo services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/ExtOfferResourceHome.java
echo services/GORFX/src/de/zib/gndms/GORFX/offer/service/OfferImpl.java
echo services/GORFX/src/de/zib/gndms/GORFX/context/service/TaskImpl.java
echo services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/TaskResource.java
echo services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/ExtTaskResourceHome.java
