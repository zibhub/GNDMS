/**
 * SubspacePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subspace.stubs;

public interface SubspacePortType extends java.rmi.Remote {
    public de.zib.gndms.dspace.subspace.stubs.CreateSliceResponse createSlice(de.zib.gndms.dspace.subspace.stubs.CreateSliceRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.stubs.types.InternalFailure, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
    public de.zib.gndms.dspace.subspace.stubs.ListCreatableSliceKindsResponse listCreatableSliceKinds(de.zib.gndms.dspace.subspace.stubs.ListCreatableSliceKindsRequest parameters) throws java.rmi.RemoteException;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
}
