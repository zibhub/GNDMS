source $(dirname $0)/var-check.sh
var_check

if [ -z "$*" ]; then 
	CMD="cat -" 
else 
	if [ "$*" = "all" ]; then 
		ALL="yes"
		CMD="cat -"
	else
		CMD="grep -i $*"
	fi
fi

print_sources() {
echo $C3GRID_SOURCE/sync.sh
echo $C3GRID_SOURCE/build-deploy.xml
find $C3GRID_SOURCE/scripts -type f
find $C3GRID_SOURCE/types/global -type f
find $C3GRID_SOURCE/etc -type f
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

if [ ! -z "$ALL" ] ; then 
	find $C3GRID_SOURCE/services/*/schema -type f
	find $C3GRID_SOURCE/services/*/src -type f
	find $C3GRID_SOURCE/services/*/*.xml -type f
	find $C3GRID_SOURCE/services/*/*.wsdd -type f
else
echo $C3GRID_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/service/globus/resource/ExtLOFISResourceHome.java
echo $C3GRID_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/service/globus/resource/LOFISResource.java
echo $C3GRID_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/lofiset/service/globus/resource/ExtLofiSetResourceHome.java
echo $C3GRID_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/lofiset/service/globus/resource/LofiSetResource.java
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
fi
}

print_sources | $CMD
