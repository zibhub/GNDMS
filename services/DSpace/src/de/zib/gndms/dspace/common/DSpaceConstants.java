package de.zib.gndms.dspace.common;

import javax.xml.namespace.QName;


public interface DSpaceConstants {
	public static final String SERVICE_NS = "http://dspace.gndms.zib.de/dspace";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "DSpaceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "DSpaceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
