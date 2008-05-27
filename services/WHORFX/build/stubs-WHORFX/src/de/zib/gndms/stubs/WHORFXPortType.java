/**
 * WHORFXPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.stubs;

public interface WHORFXPortType extends java.rmi.Remote {
    public de.zib.gndms.stubs.LookupORFResponse lookupORF(de.zib.gndms.stubs.LookupORFRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.stubs.types.UnknownORFType;
    public de.zib.gndms.stubs.UpdateMappingsResponse updateMappings(de.zib.gndms.stubs.UpdateMappingsRequest parameters) throws java.rmi.RemoteException;
    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
}
