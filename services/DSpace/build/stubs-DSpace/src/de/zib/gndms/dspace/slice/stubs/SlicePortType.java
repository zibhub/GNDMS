/**
 * SlicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs;

public interface SlicePortType extends java.rmi.Remote {
    public de.zib.gndms.dspace.slice.stubs.TransformSliceToResponse transformSliceTo(de.zib.gndms.dspace.slice.stubs.TransformSliceToRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
    public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy destroyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceUnknownFaultType, org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType;
    public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime setTerminationTimeRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceUnknownFaultType, org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType, org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType;
    public org.oasis.wsn.SubscribeResponse subscribe(org.oasis.wsn.Subscribe subscribeRequest) throws java.rmi.RemoteException, org.oasis.wsn.TopicNotSupportedFaultType, org.oasis.wsn.SubscribeCreationFailedFaultType, org.oasis.wsn.InvalidTopicExpressionFaultType, org.oasis.wsn.ResourceUnknownFaultType, org.oasis.wsn.TopicPathDialectUnknownFaultType;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
}
