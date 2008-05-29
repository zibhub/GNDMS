package de.zib.gndms.lofis.lofiset.common;

import javax.xml.namespace.QName;


public interface LofiSetConstants {
	public static final String SERVICE_NS = "http://lofis.gndms.zib.de/LOFIS/LofiSet";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "LofiSetKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "LofiSetResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName CURRENTTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime");
	public static final QName TERMINATIONTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime");
	public static final QName LOFISEQ = new QName("http://gndms.zib.de/common/types", "LofiSeq");
	
}
