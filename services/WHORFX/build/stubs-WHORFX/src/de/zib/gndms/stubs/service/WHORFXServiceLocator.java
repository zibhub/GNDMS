/**
 * WHORFXServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Nov 14, 2006 (10:23:53 EST) WSDL2Java emitter.
 */

package de.zib.gndms.stubs.service;

public class WHORFXServiceLocator extends org.apache.axis.client.Service implements de.zib.gndms.stubs.service.WHORFXService {

    public WHORFXServiceLocator() {
    }


    public WHORFXServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WHORFXServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WHORFXPortTypePort
    private java.lang.String WHORFXPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getWHORFXPortTypePortAddress() {
        return WHORFXPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WHORFXPortTypePortWSDDServiceName = "WHORFXPortTypePort";

    public java.lang.String getWHORFXPortTypePortWSDDServiceName() {
        return WHORFXPortTypePortWSDDServiceName;
    }

    public void setWHORFXPortTypePortWSDDServiceName(java.lang.String name) {
        WHORFXPortTypePortWSDDServiceName = name;
    }

    public de.zib.gndms.stubs.WHORFXPortType getWHORFXPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WHORFXPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWHORFXPortTypePort(endpoint);
    }

    public de.zib.gndms.stubs.WHORFXPortType getWHORFXPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.zib.gndms.stubs.bindings.WHORFXPortTypeSOAPBindingStub _stub = new de.zib.gndms.stubs.bindings.WHORFXPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getWHORFXPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWHORFXPortTypePortEndpointAddress(java.lang.String address) {
        WHORFXPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.zib.gndms.stubs.WHORFXPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.zib.gndms.stubs.bindings.WHORFXPortTypeSOAPBindingStub _stub = new de.zib.gndms.stubs.bindings.WHORFXPortTypeSOAPBindingStub(new java.net.URL(WHORFXPortTypePort_address), this);
                _stub.setPortName(getWHORFXPortTypePortWSDDServiceName());
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
        if ("WHORFXPortTypePort".equals(inputPortName)) {
            return getWHORFXPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gndms.zib.de/WHORFX/service", "WHORFXService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gndms.zib.de/WHORFX/service", "WHORFXPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("WHORFXPortTypePort".equals(portName)) {
            setWHORFXPortTypePortEndpointAddress(address);
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
