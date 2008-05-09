package de.zib.gndms.common;

import javax.xml.namespace.QName;


public interface WHORFXConstants {
	public static final String SERVICE_NS = "http://gndms.zib.de/WHORFX";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "WHORFXKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "WHORFXResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
