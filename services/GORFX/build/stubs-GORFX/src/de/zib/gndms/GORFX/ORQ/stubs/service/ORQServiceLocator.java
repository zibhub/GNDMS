/**
 * ORQServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.ORQ.stubs.service;

public class ORQServiceLocator extends org.apache.axis.client.Service implements de.zib.gndms.GORFX.ORQ.stubs.service.ORQService {

    public ORQServiceLocator() {
    }


    public ORQServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ORQServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ORQPortTypePort
    private java.lang.String ORQPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getORQPortTypePortAddress() {
        return ORQPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ORQPortTypePortWSDDServiceName = "ORQPortTypePort";

    public java.lang.String getORQPortTypePortWSDDServiceName() {
        return ORQPortTypePortWSDDServiceName;
    }

    public void setORQPortTypePortWSDDServiceName(java.lang.String name) {
        ORQPortTypePortWSDDServiceName = name;
    }

    public de.zib.gndms.GORFX.ORQ.stubs.ORQPortType getORQPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ORQPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getORQPortTypePort(endpoint);
    }

    public de.zib.gndms.GORFX.ORQ.stubs.ORQPortType getORQPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.zib.gndms.GORFX.ORQ.stubs.bindings.ORQPortTypeSOAPBindingStub _stub = new de.zib.gndms.GORFX.ORQ.stubs.bindings.ORQPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getORQPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setORQPortTypePortEndpointAddress(java.lang.String address) {
        ORQPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.zib.gndms.GORFX.ORQ.stubs.ORQPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.zib.gndms.GORFX.ORQ.stubs.bindings.ORQPortTypeSOAPBindingStub _stub = new de.zib.gndms.GORFX.ORQ.stubs.bindings.ORQPortTypeSOAPBindingStub(new java.net.URL(ORQPortTypePort_address), this);
                _stub.setPortName(getORQPortTypePortWSDDServiceName());
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
        if ("ORQPortTypePort".equals(inputPortName)) {
            return getORQPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ/service", "ORQService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ/service", "ORQPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("ORQPortTypePort".equals(portName)) {
            setORQPortTypePortEndpointAddress(address);
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
