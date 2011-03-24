enable_interslicetransfer() {
moni call -v .gorfx.SetupOfferType "\
	offerType:'http://gndms.zib.de/ORQTypes/InterSliceTransfer'; \
	orqType:'{http://gndms.zib.de/c3grid/types}InterSliceTransferORQT'; \
	resType:'{http://gndms.zib.de/c3grid/types}InterSliceTransferResultT'; \
	calcFactory:'de.zib.gndms.GORFX.action.InterSliceTransferORQFactory'; \
	taskActionFactory:'de.zib.gndms.GORFX.action.InterSliceTransferActionFactory'; \
	mode:'$MODE'"
}
