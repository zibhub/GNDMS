package de.zib.gndms.gritserv.delegation;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.globus.delegation.DelegationListener;
import org.globus.delegation.DelegationException;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.*;
import org.apache.log4j.Logger;

import javax.naming.NamingException;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 10.02.2009, Time: 14:58:55
 */
public class GNDMSDelegationListener implements DelegationListener {

    private static Logger logger = Logger.getLogger( GNDMSDelegationListener.class );
    private String regristrationId;
    private ResourceKey resourceKey;
    private String homeName;
    private static final long serialVersionUID = -697463760099486213L;


    public GNDMSDelegationListener() {
    }


    public GNDMSDelegationListener( final ResourceKey resourceKey, final String homeName ) {
        this.resourceKey = resourceKey;
        this.homeName = homeName;
    }


    public void setCredential( final GlobusCredential credential ) throws DelegationException {

        try{
            GNDMSCredibleResource res = null;
                res = ( GNDMSCredibleResource ) getResourceHome().find( resourceKey );
                res.setCredential( credential );
        } catch ( NamingException e ) {
            logger.error( e );
            throw new DelegationException( e );
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


    public String getHomeName() {
        return homeName;
    }


    public void setHomeName( final String homeName ) {
        this.homeName = homeName;
    }


    public ResourceHome getResourceHome () throws NamingException {

        org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
        String servicePath = ctx.getTargetService();

        javax.naming.Context initialContext = new javax.naming.InitialContext();
        return (ResourceHome) initialContext.lookup( getHomeName() );
    }
}
