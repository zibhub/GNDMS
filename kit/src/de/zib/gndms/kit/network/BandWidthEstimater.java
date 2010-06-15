package de.zib.gndms.kit.network;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 11:33:40
 */
public interface BandWidthEstimater {

    /**
     * Estimates the bandwidth between to given hosts.
     *
     * @return Returns the estimaed speed in byte/s or NULL if there is
     *         they are not connected. (Maybe because one of the hosts doesn't exist)
     */
    public Float estimateBandWidthFromTo( String src, String tgt );
}
