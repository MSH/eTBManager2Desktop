/**
 * AuthenticatorService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.msh.etbm.webservices.authenticator;

public interface AuthenticatorService_PortType extends java.rmi.Remote {
    public Response getUserWorkspaces(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public Response login(java.lang.String username, java.lang.String password, int workspaceId) throws java.rmi.RemoteException;
}
