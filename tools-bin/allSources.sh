find logic/src -type f
find logic/test-src -type f
find model/src -type f
find model/test-src -type f
find infra/src -type f
find infra/test-src -type f
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
