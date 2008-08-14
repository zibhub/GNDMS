package de.zib.gndms.dspace.subspace.common;

import javax.xml.namespace.QName;


public interface SubspaceConstants {
	public static final String SERVICE_NS = "http://dspace.gndms.zib.de/dspace/Subspace";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "SubspaceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "SubspaceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName AVAILABLESTORAGESIZE = new QName("http://gndms.zib.de/common/types", "AvailableStorageSize");
	public static final QName TOTALSTORAGESIZE = new QName("http://gndms.zib.de/common/types", "TotalStorageSize");
	public static final QName DSPACEREFERENCE = new QName("http://dspace.gndms.zib.de/dspace/types", "DSpaceReference");
	public static final QName SUBSPACESPECIFIER = new QName("http://gndms.zib.de/common/types", "SubspaceSpecifier");
	
}
