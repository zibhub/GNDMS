package de.zib.gndms.infra.service;

import de.zib.gndms.model.common.GridResource;
import org.globus.wsrf.ResourceException;


/**
 * Marker interface; to be extended later
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 18:29:01
 */
@SuppressWarnings({ "MarkerInterface" })
public interface GNDMSingletonServiceHome<H extends GridResource> extends GNDMServiceHome<H> {
    String getSingletonID() throws ResourceException;
}
