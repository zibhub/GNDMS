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

print_stuff_sources() {
find $GNDMS_SOURCE/stuff/src -type f
find $GNDMS_SOURCE/stuff/test-src -type f
}

print_model_source() {
find $GNDMS_SOURCE/model/src -type f
find $GNDMS_SOURCE/model/test-src -type f
}

print_kit_sources() {
find $GNDMS_SOURCE/kit/src -type f
find $GNDMS_SOURCE/kit/test-src -type f
}

print_logic_sources() {
find $GNDMS_SOURCE/logic/src -type f
find $GNDMS_SOURCE/logic/test-src -type f
}

print_infra_sources() {
find $GNDMS_SOURCE/infra/src -type f
find $GNDMS_SOURCE/infra/test-src -type f
}

print_typecon_sources() {
find $GNDMS_SOURCE/typecon/src -type f
find $GNDMS_SOURCE/typecon/test-src -type f
}

print_DSpace_sources() {
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/DSpaceResource.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/globus/resource/ExtDSpaceResourceHome.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/service/DSpaceImpl.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/SubspaceResource.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/globus/resource/ExtSubspaceResourceHome.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/subspace/service/SubspaceImpl.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/SliceResource.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/globus/resource/ExtSliceResourceHome.java
echo $GNDMS_SOURCE/services/DSpace/src/de/zib/gndms/dspace/slice/service/SliceImpl.java
}

print_GORFX_sources() {
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/GORFXResource.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/globus/resource/ExtGORFXResourceHome.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/service/GORFXImpl.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ORQResource.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/globus/resource/ExtORQResourceHome.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/ORQ/service/ORQImpl.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/OfferResource.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/globus/resource/ExtOfferResourceHome.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/offer/service/OfferImpl.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/TaskImpl.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/TaskResource.java
echo $GNDMS_SOURCE/services/GORFX/src/de/zib/gndms/GORFX/context/service/globus/resource/ExtTaskResourceHome.java
}

print_sources() {
echo $GNDMS_SOURCE/sync.sh
echo $GNDMS_SOURCE/build-deploy.xml
find $GNDMS_SOURCE/scripts -type f
find $GNDMS_SOURCE/types/global -type f
find $GNDMS_SOURCE/etc -type f
print_infra_sources
print_logic_sources
print_model_source
print_kit_sources
print_stuff_sources
find $GNDMS_SOURCE/services/*/test/src -type f | grep -v README.txt

if [ ! -z "$ALL" ] ; then 
	find $GNDMS_SOURCE/services/*/schema -type f
	find $GNDMS_SOURCE/services/*/src -type f
	find $GNDMS_SOURCE/services/*/*.xml -type f
	find $GNDMS_SOURCE/services/*/*.wsdd -type f
else
echo $GNDMS_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/service/globus/resource/ExtLOFISResourceHome.java
echo $GNDMS_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/service/globus/resource/LOFISResource.java
echo $GNDMS_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/lofiset/service/globus/resource/ExtLofiSetResourceHome.java
echo $GNDMS_SOURCE/services/LOFIS/src/de/zib/gndms/lofis/lofiset/service/globus/resource/LofiSetResource.java
echo $GNDMS_SOURCE/services/WHORFX/src/de/zib/gndms/service/WHORFXImpl.java
print_DSpace_sources
print_GORFX_sources
fi
}

print_sources | $CMD
