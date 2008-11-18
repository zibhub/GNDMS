enable_republishslice() {
moni call -v .gorfx.SetupOfferType "offerType:'http://www.c3grid.de/ORQTypes/RePublishSlice'; orqType:'{http://gndms.zib.de/c3grid/types}RePublishSliceORQT'; resType:'{http://gndms.zib.de/c3grid/types}RePublishSliceResultT'; calcFactory:'de.zib.gndms.GORFX.action.RePublishSliceORQFactory'; taskActionFactory:'de.zib.gndms.GORFX.action.RePublishSliceActionFactory'; mode:'$MODE'"
}
