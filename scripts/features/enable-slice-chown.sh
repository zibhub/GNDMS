#!/bin/sh
enable_sliceChown() {
    moni call -v .sys.SetupDefaultConfiglet "\
        mode: '$MODE'; \
        name: 'sliceChown'; \
        className: 'de.zib.gndms.logic.model.dspace.ChownSliceConfiglet'; \
        chownScript: $GNDMS_SHARED/chownSlice.sh;"
}
#vim:tw=0
