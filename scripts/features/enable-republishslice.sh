enable_republishslice() {
moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/RePublishSliceTransfer'; orqType: '{http://gndms.zib.de/c3grid/types}RePublishSliceTransferORQT'; resType: '{http://gndms.zib.de/c3grid/types}RePublishSliceTransferResultT'; calcFactory: de.zib.gndms.GORFX.action.RePublishSliceTransferORQFactory; taskActionFactory: de.zib.gndms.GORFX.action.RePublishSliceTransferActionFactory; mod
e:'$MODE'"
}
