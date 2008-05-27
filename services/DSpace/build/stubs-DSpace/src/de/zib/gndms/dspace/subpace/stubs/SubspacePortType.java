/**
 * SubspacePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subpace.stubs;

public interface SubspacePortType extends java.rmi.Remote {
    public de.zib.gndms.dspace.subpace.stubs.CreateSliceResponse createSlice(de.zib.gndms.dspace.subpace.stubs.CreateSliceRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.subpace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subpace.stubs.types.UnsupportedOrInvalidSliceKind;
    public de.zib.gndms.dspace.subpace.stubs.GetSliceByIdResponse getSliceById(de.zib.gndms.dspace.subpace.stubs.GetSliceByIdRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.subpace.stubs.types.UnknownSliceId;
    public de.zib.gndms.dspace.subpace.stubs.ListCreatableSliceKindsResponse listCreatableSliceKinds(de.zib.gndms.dspace.subpace.stubs.ListCreatableSliceKindsRequest parameters) throws java.rmi.RemoteException;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
    public org.oasis.wsn.SubscribeResponse subscribe(org.oasis.wsn.Subscribe subscribeRequest) throws java.rmi.RemoteException, org.oasis.wsn.TopicNotSupportedFaultType, org.oasis.wsn.InvalidTopicExpressionFaultType, org.oasis.wsn.SubscribeCreationFailedFaultType, org.oasis.wsn.ResourceUnknownFaultType, org.oasis.wsn.TopicPathDialectUnknownFaultType;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType;
}
