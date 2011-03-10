package de.zib.gndms.dspace.subspace.common;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import javax.xml.namespace.QName;


public interface SubspaceConstants {
	public static final String SERVICE_NS = "http://dspace.gndms.zib.de/DSpace/Subspace";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "SubspaceKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "SubspaceResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName AVAILABLESTORAGESIZE = new QName("http://gndms.zib.de/common/types", "AvailableStorageSize");
	public static final QName TOTALSTORAGESIZE = new QName("http://gndms.zib.de/common/types", "TotalStorageSize");
	public static final QName DSPACEREFERENCE = new QName("http://dspace.gndms.zib.de/DSpace/types", "DSpaceReference");
	public static final QName SUBSPACESPECIFIER = new QName("http://gndms.zib.de/common/types", "SubspaceSpecifier");
	
}
