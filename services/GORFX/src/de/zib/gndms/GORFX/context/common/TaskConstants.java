package de.zib.gndms.GORFX.context.common;

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
