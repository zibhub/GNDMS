enable_slicestagein() {

moni call -v .dspace.SetupSubspace   "subspace:'{http://www.c3grid.de/G2/Subspace}DMS'; \
	path:'$DMS_AREA_PATH'; \
	gsiFtpPath: '$DMS_AREA_GSI_FTP_URL'; \
	visible:true; \
	size:'$DMS_AREA_SIZE'; \
	mode:'$MODE'"

moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}DMS'; sliceKind: http://www.c3grid.de/G2/SliceKind/DMS; mode:'$ADDMODE'"

moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}DMS'; sliceKind: http://www.c3grid.de/G2/SliceKind/DMS_RW; mode:'$ADDMODE'"

moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/SliceStageIn'; orqType: '{http://gndms.zib.de/c3grid/types}SliceStageInORQT'; resType: '{http://gndms.zib.de/c3grid/types}SliceStageInResultT'; calcFactory: de.zib.gndms.GORFX.action.dms.SliceStageInORQFactory; taskActionFactory: de.zib.gndms.GORFX.action.dms.SliceStageInActionFactory; mode:'$MODE'"
}
