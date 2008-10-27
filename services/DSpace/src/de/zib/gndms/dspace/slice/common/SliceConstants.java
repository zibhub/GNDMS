package de.zib.gndms.dspace.slice.common;

import javax.xml.namespace.QName;


public interface SliceConstants {
	public static final String SERVICE_NS = "http://dspace.gndms.zib.de/DSpace/Slice";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "SliceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "SliceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName CURRENTTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime");
	public static final QName TERMINATIONTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime");
	public static final QName TOTALSTORAGESIZE = new QName("http://gndms.zib.de/common/types", "TotalStorageSize");
	public static final QName SLICEKIND = new QName("http://gndms.zib.de/common/types", "SliceKind");
	public static final QName SLICELOCATION = new QName("http://gndms.zib.de/common/types", "SliceLocation");
	public static final QName SUBSPACEREFERENCE = new QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "SubspaceReference");
	
}
