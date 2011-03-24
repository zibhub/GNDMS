package de.zib.gndmc.GORFX.constants;

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


public interface TaskConstants {
	public static final String SERVICE_NS = "http://GORFX.gndms.zib.de/GORFX/Context";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "TaskKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "TaskResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName CURRENTTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime");
	public static final QName TERMINATIONTIME = new QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime");
	public static final QName TASKEXECUTIONSTATE = new QName("http://gndms.zib.de/common/types", "TaskExecutionState");
	public static final QName TASKEXECUTIONFAILURE = new QName("http://gndms.zib.de/common/types", "TaskExecutionFailure");
	public static final QName TASKEXECUTIONRESULTS = new QName("http://gndms.zib.de/common/types", "TaskExecutionResults");

}
