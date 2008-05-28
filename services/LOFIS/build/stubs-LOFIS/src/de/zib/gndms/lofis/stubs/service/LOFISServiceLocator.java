/**
 * LOFISServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.lofis.stubs.service;

public class LOFISServiceLocator extends org.apache.axis.client.Service implements de.zib.gndms.lofis.stubs.service.LOFISService {

    public LOFISServiceLocator() {
    }


    public LOFISServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LOFISServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LOFISPortTypePort
    private java.lang.String LOFISPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getLOFISPortTypePortAddress() {
        return LOFISPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LOFISPortTypePortWSDDServiceName = "LOFISPortTypePort";

    public java.lang.String getLOFISPortTypePortWSDDServiceName() {
        return LOFISPortTypePortWSDDServiceName;
    }

    public void setLOFISPortTypePortWSDDServiceName(java.lang.String name) {
        LOFISPortTypePortWSDDServiceName = name;
    }

    public de.zib.gndms.lofis.stubs.LOFISPortType getLOFISPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LOFISPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLOFISPortTypePort(endpoint);
    }

    public de.zib.gndms.lofis.stubs.LOFISPortType getLOFISPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.zib.gndms.lofis.stubs.bindings.LOFISPortTypeSOAPBindingStub _stub = new de.zib.gndms.lofis.stubs.bindings.LOFISPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getLOFISPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLOFISPortTypePortEndpointAddress(java.lang.String address) {
        LOFISPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.zib.gndms.lofis.stubs.LOFISPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.zib.gndms.lofis.stubs.bindings.LOFISPortTypeSOAPBindingStub _stub = new de.zib.gndms.lofis.stubs.bindings.LOFISPortTypeSOAPBindingStub(new java.net.URL(LOFISPortTypePort_address), this);
                _stub.setPortName(getLOFISPortTypePortWSDDServiceName());
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
        if ("LOFISPortTypePort".equals(inputPortName)) {
            return getLOFISPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://lofis.gndms.zib.de/LOFIS/service", "LOFISService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://lofis.gndms.zib.de/LOFIS/service", "LOFISPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("LOFISPortTypePort".equals(portName)) {
            setLOFISPortTypePortEndpointAddress(address);
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
