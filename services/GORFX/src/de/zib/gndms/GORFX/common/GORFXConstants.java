package de.zib.gndms.GORFX.common;

import javax.xml.namespace.QName;


public interface GORFXConstants {
	public static final String SERVICE_NS = "http://GORFX.gndms.zib.de/GORFX";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GORFXKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "GORFXResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
