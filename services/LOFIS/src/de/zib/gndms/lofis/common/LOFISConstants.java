package de.zib.gndms.lofis.common;

import javax.xml.namespace.QName;


public interface LOFISConstants {
	public static final String SERVICE_NS = "http://lofis.gndms.zib.de/LOFIS";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "LOFISKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "LOFISResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
