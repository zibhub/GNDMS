package de.zib.gndms.kit.network;

/**
 * This class is for developing and testing purpose only.
 * Its estimateBandWidthFromTo-method returns a fixed value, which is
 * independent of the given hosts.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 10:29:43
 */
public class StaticBandWidthEstimater implements BandWidthEstimater {

    // lets asume we have 10Mbit/s approx 1 250 000 byte/s ;
    private final Float bandWidth =  new Float( 10 * 1000*1000 / 8 );
    
    public Float estimateBandWidthFromTo( String src, String tgt ) {
        return bandWidth;
    }
}
