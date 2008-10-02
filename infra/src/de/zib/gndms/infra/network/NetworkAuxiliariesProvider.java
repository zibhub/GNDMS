package de.zib.gndms.infra.network;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 10:51:58
 */
public class NetworkAuxiliariesProvider {

    private final GridFTPClientFactory gridFTPClientFactory = new SimpleGridFTPClientFactory();
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
        return  Float.valueOf( size * bandWidth ).intValue( );
    }
}
