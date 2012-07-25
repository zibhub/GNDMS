c3grid_generic() { 
   RO="500" 
   RW="700" 
	if [ -z "$ADDON" ] ; then
		moni call -v .dspace.SetupSliceKind "\
			sliceKind:'http://www.c3grid.de/G2/SliceKind/DMS'; \
			sliceKindMode:$RO; \
			timeToLive: $TTL; \
            uniqueDirName: dmsROSlices; \
			mode: '$MODE'"
		
		moni call -v .dspace.SetupSliceKind "\
			sliceKind:'http://www.c3grid.de/G2/SliceKind/DMS_RW'; \
			sliceKindMode:$RW; \
			timeToLive: $TTL; \
            uniqueDirName: dmsRWSlices; \
			mode: '$MODE'"
    fi
}
