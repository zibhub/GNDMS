package de.zib.gndms.comserv.delegation;

import org.globus.delegation.DelegationListener;
import org.globus.delegation.DelegationException;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.*;
import org.apache.log4j.Logger;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 10.02.2009, Time: 14:58:55
 */
public class GNDMSDelegationListener implements DelegationListener {

    private static Logger logger = Logger.getLogger( GNDMSDelegationListener.class );
    private String regristrationId;
    private ResourceKey resourceKey;
    transient private ResourceHome home;


    public GNDMSDelegationListener() {
    }


    public GNDMSDelegationListener( final ResourceKey resourceKey, final ResourceHome home ) {
        this.resourceKey = resourceKey;
        this.home = home;
    }


    public void setCredential( final GlobusCredential credential ) throws DelegationException {

        try{
            GNDMSCredibleResource res = ( GNDMSCredibleResource ) home.find( resourceKey );
            res.setCredential( credential );
        } catch ( ResourceException e ) {
            logger.error( e );
        }
    }


    public void setId( final String s ) {
        regristrationId = s;
    }


    public String getId() {
        return regristrationId;
    }


    public void credentialDeleted() {
        // Not required here yet
    }


    public ResourceKey getResourceKey() {
        return resourceKey;
    }


    public void setResourceKey( final ResourceKey resourceKey ) {
        this.resourceKey = resourceKey;
    }


    public ResourceHome getHome() {
        return home;
    }


    public void setHome( final ResourceHome home ) {
        this.home = home;
    }
}
