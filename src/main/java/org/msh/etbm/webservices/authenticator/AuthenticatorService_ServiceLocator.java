/**
 * AuthenticatorService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.msh.etbm.webservices.authenticator;

public class AuthenticatorService_ServiceLocator extends org.apache.axis.client.Service implements AuthenticatorService_Service {
	private static final long serialVersionUID = 7328705853467400357L;

	public AuthenticatorService_ServiceLocator() {
    }


    public AuthenticatorService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AuthenticatorService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for authenticatorServicePort
    private java.lang.String authenticatorServicePort_address = "http://localhost:8080/etbmanager/services/authentication";

    public java.lang.String getauthenticatorServicePortAddress() {
        return authenticatorServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String authenticatorServicePortWSDDServiceName = "authenticatorServicePort";

    public java.lang.String getauthenticatorServicePortWSDDServiceName() {
        return authenticatorServicePortWSDDServiceName;
    }

    public void setauthenticatorServicePortWSDDServiceName(java.lang.String name) {
        authenticatorServicePortWSDDServiceName = name;
    }

    public AuthenticatorService_PortType getauthenticatorServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(authenticatorServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getauthenticatorServicePort(endpoint);
    }

    public AuthenticatorService_PortType getauthenticatorServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            AuthenticatorServiceBindingStub _stub = new AuthenticatorServiceBindingStub(portAddress, this);
            _stub.setPortName(getauthenticatorServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setauthenticatorServicePortEndpointAddress(java.lang.String address) {
        authenticatorServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (AuthenticatorService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                AuthenticatorServiceBindingStub _stub = new AuthenticatorServiceBindingStub(new java.net.URL(authenticatorServicePort_address), this);
                _stub.setPortName(getauthenticatorServicePortWSDDServiceName());
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
        if ("authenticatorServicePort".equals(inputPortName)) {
            return getauthenticatorServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://etbmanager.org/authenticator", "authenticatorService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://etbmanager.org/authenticator", "authenticatorServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("authenticatorServicePort".equals(portName)) {
            setauthenticatorServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
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
