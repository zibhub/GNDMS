/**
 * SubspaceServiceAddressingLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subpace.stubs.service;

public class SubspaceServiceAddressingLocator extends de.zib.gndms.dspace.subpace.stubs.service.SubspaceServiceLocator implements de.zib.gndms.dspace.subpace.stubs.service.SubspaceServiceAddressing {
    public de.zib.gndms.dspace.subpace.stubs.SubspacePortType getSubspacePortTypePort(org.apache.axis.message.addressing.EndpointReferenceType reference) throws javax.xml.rpc.ServiceException {
	org.apache.axis.message.addressing.AttributedURI address = reference.getAddress();
	if (address == null) {
		throw new javax.xml.rpc.ServiceException("No address in EndpointReference");
	}
	java.net.URL endpoint;
	try {
		endpoint = new java.net.URL(address.toString());
	} catch (java.net.MalformedURLException e) {
		throw new javax.xml.rpc.ServiceException(e);
	}
	de.zib.gndms.dspace.subpace.stubs.SubspacePortType _stub = getSubspacePortTypePort(endpoint);
	if (_stub != null) {
		org.apache.axis.message.addressing.AddressingHeaders headers =
			new org.apache.axis.message.addressing.AddressingHeaders();
		headers.setTo(address);
		headers.setReferenceProperties(reference.getProperties());
		((javax.xml.rpc.Stub)_stub)._setProperty(org.apache.axis.message.addressing.Constants.ENV_ADDRESSING_SHARED_HEADERS, headers);
	}
	return _stub;
    }


}
