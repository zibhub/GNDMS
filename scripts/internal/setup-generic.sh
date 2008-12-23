setup_generic() { 
   RO="500" 
   RW="700" 
	if [ -z "$ADDON" ] ; then
		moni call -v .dspace.SetupSliceKind "\
			sliceKind:'http://www.c3grid.de/G2/SliceKind/DMS'; \
			sliceKindMode:$RO; \
			mode: '$MODE'"
		
		moni call -v .dspace.SetupSliceKind "\
			sliceKind:'http://www.c3grid.de/G2/SliceKind/DMS_RW'; \
			sliceKindMode:$RW; \
			mode: '$MODE'"
    fi
}

