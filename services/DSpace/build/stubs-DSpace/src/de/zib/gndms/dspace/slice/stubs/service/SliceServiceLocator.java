/**
 * SliceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs.service;

public class SliceServiceLocator extends org.apache.axis.client.Service implements de.zib.gndms.dspace.slice.stubs.service.SliceService {

    public SliceServiceLocator() {
    }


    public SliceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SliceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SlicePortTypePort
    private java.lang.String SlicePortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getSlicePortTypePortAddress() {
        return SlicePortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SlicePortTypePortWSDDServiceName = "SlicePortTypePort";

    public java.lang.String getSlicePortTypePortWSDDServiceName() {
        return SlicePortTypePortWSDDServiceName;
    }

    public void setSlicePortTypePortWSDDServiceName(java.lang.String name) {
        SlicePortTypePortWSDDServiceName = name;
    }

    public de.zib.gndms.dspace.slice.stubs.SlicePortType getSlicePortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SlicePortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSlicePortTypePort(endpoint);
    }

    public de.zib.gndms.dspace.slice.stubs.SlicePortType getSlicePortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.zib.gndms.dspace.slice.stubs.bindings.SlicePortTypeSOAPBindingStub _stub = new de.zib.gndms.dspace.slice.stubs.bindings.SlicePortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getSlicePortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSlicePortTypePortEndpointAddress(java.lang.String address) {
        SlicePortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.zib.gndms.dspace.slice.stubs.SlicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.zib.gndms.dspace.slice.stubs.bindings.SlicePortTypeSOAPBindingStub _stub = new de.zib.gndms.dspace.slice.stubs.bindings.SlicePortTypeSOAPBindingStub(new java.net.URL(SlicePortTypePort_address), this);
                _stub.setPortName(getSlicePortTypePortWSDDServiceName());
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
        if ("SlicePortTypePort".equals(inputPortName)) {
            return getSlicePortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/service", "SliceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/service", "SlicePortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("SlicePortTypePort".equals(portName)) {
            setSlicePortTypePortEndpointAddress(address);
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
