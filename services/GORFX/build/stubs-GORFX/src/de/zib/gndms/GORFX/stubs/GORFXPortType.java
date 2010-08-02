/**
 * GORFXPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.stubs;

public interface GORFXPortType extends java.rmi.Remote {
    public de.zib.gndms.GORFX.stubs.CreateOfferRequestResponse createOfferRequest(de.zib.gndms.GORFX.stubs.CreateOfferRequestRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.GORFX.stubs.types.UnsupportedOfferType;
    public de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesResponse getSupportedOfferTypes(de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequest parameters) throws java.rmi.RemoteException;
    public de.zib.gndms.GORFX.stubs.CallMaintenanceActionResponse callMaintenanceAction(de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequest parameters) throws java.rmi.RemoteException;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
}
