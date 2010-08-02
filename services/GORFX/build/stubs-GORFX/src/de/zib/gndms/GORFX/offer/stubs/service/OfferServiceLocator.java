/**
 * OfferServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.offer.stubs.service;

public class OfferServiceLocator extends org.apache.axis.client.Service implements de.zib.gndms.GORFX.offer.stubs.service.OfferService {

    public OfferServiceLocator() {
    }


    public OfferServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OfferServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OfferPortTypePort
    private java.lang.String OfferPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getOfferPortTypePortAddress() {
        return OfferPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OfferPortTypePortWSDDServiceName = "OfferPortTypePort";

    public java.lang.String getOfferPortTypePortWSDDServiceName() {
        return OfferPortTypePortWSDDServiceName;
    }

    public void setOfferPortTypePortWSDDServiceName(java.lang.String name) {
        OfferPortTypePortWSDDServiceName = name;
    }

    public de.zib.gndms.GORFX.offer.stubs.OfferPortType getOfferPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OfferPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOfferPortTypePort(endpoint);
    }

    public de.zib.gndms.GORFX.offer.stubs.OfferPortType getOfferPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.zib.gndms.GORFX.offer.stubs.bindings.OfferPortTypeSOAPBindingStub _stub = new de.zib.gndms.GORFX.offer.stubs.bindings.OfferPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getOfferPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOfferPortTypePortEndpointAddress(java.lang.String address) {
        OfferPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.zib.gndms.GORFX.offer.stubs.OfferPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.zib.gndms.GORFX.offer.stubs.bindings.OfferPortTypeSOAPBindingStub _stub = new de.zib.gndms.GORFX.offer.stubs.bindings.OfferPortTypeSOAPBindingStub(new java.net.URL(OfferPortTypePort_address), this);
                _stub.setPortName(getOfferPortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OfferPortTypePort".equals(inputPortName)) {
            return getOfferPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/Offer/service", "OfferService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/Offer/service", "OfferPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("OfferPortTypePort".equals(portName)) {
            setOfferPortTypePortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
