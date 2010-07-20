package de.zib.gndms.model.common;

import javax.persistence.Embeddable;
import javax.xml.namespace.QName;

/**
 * VEPRefs for grid resources that use a SimpleResourceKey with a UUID string value
 * (i.e. resource instances from introduce-generated services)
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:03:31
 */
@Embeddable
public abstract class SimpleRKRef extends VEPRef {

    public abstract QName getResourceKeyName();

    public abstract String getResourceKeyValue();
    public abstract void setResourceKeyValue(final String newValue);
}

