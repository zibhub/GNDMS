/**
 * ORQPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.ORQ.stubs;

public interface ORQPortType extends java.rmi.Remote {
    public de.zib.gndms.GORFX.ORQ.stubs.GetOfferAndDestroyRequestResponse getOfferAndDestroyRequest(de.zib.gndms.GORFX.ORQ.stubs.GetOfferAndDestroyRequestRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied, de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest;
    public de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestResponse permitEstimateAndDestroyRequest(de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.GORFX.ORQ.stubs.types.PermissionDenied, de.zib.gndms.GORFX.ORQ.stubs.types.UnfullfillableRequest;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
    public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy destroyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceUnknownFaultType, org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType;
    public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime setTerminationTimeRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceUnknownFaultType, org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType, org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
}
