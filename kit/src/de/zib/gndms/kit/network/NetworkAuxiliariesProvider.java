package de.zib.gndms.kit.network;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 10:51:58
 *
 * Usage of the network package for a file transfer:
 *
 * Use the GridFTPClientFactory provided by this class to create an GridFTPClient.
 * Given that this class is correctly set up and returns the desired factory, the GridFTPClient, should be configured to
 * your needs, e.g. with the correct security setup.
 *
 * Then create a GNDMSFileTransfer, and add a map with the files which should be transferred, the source and destination
 * directory and client to the instance.
 *
 * If it is of interest how big the download is, you can uses the GNDMSFileTransfer::estimateSize method to acquire the
 * size of the files in byte. Together with a descent implementation of the BandWidthEstimater interface this can be
 * used to predict the duration of a transfer, using the calculateTransferTime method of this class.
 *
 * To make your transfer reliable you should instantiate the persistentMarkerListener, supply it with a fresh instance of
 * a FTPTransferState, which must have a unique transferId. Then use the performPersistenFileTransfer method of your
 * GNDMSFileTransfer instance to trigger the transfer.
 *
 * An aborted transfer can be easily resumed, by setting the same GNDMSFileTransfer up again,
 * Loading the transfer state object from the database, setting it into a new persistentMarkerListener and calling
 * performPersistenFileTransfer with this listener again.
 *
 * See TransferStatTest::testIt for an code example of the described procedure.
 */
public class NetworkAuxiliariesProvider {

    //private final GridFTPClientFactory gridFTPClientFactory = new SimpleGridFTPClientFactory();
    private final GridFTPClientFactory gridFTPClientFactory = new CertGridFTPClientFactory();
    private final BandWidthEstimater bandWidthEstimater = new StaticBandWidthEstimater();


    public GridFTPClientFactory getGridFTPClientFactory() {
        return gridFTPClientFactory;
    }


    public BandWidthEstimater getBandWidthEstimater() {
        return bandWidthEstimater;
    }


    /**
     * Calculated the transfertime in seconds.
     * 
     * @param size The transfer file size in byte.
     * @param bandWidth The bandwidth in byte/s
     * @return The size in sec
     */
    public static int calculateTransferTime( long size, float bandWidth  ){
        return  Float.valueOf( size / bandWidth ).intValue( );
    }
}
