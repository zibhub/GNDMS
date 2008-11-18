#!/bin/sh

enable_filetransfer() {
moni call -v .gorfx.SetupOfferType "offerType: 'http://www.c3grid.de/ORQTypes/FileTransfer'; orqType: '{http://gndms.zib.de/c3grid/types}FileTransferORQT'; resType: '{http://gndms.zib.de/c3grid/types}FileTransferResultT'; calcFactory: de.zib.gndms.logic.model.gorfx.FileTransferORQFactory; taskActionFactory: de.zib.gndms.logic.model.gorfx.FileTransferActionFactory; mode:'$MODE'"
}
