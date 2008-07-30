package de.zib.gndms.infra.db;

import org.apache.axis.types.URI;

import javax.xml.namespace.QName;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 15:58:13
 */
public interface ServiceInfo {
	URI getServiceAddress();
	QName getResourceKeyTypeName();
}
