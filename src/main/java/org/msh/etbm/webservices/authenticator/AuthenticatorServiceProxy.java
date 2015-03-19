package org.msh.etbm.webservices.authenticator;

public class AuthenticatorServiceProxy implements AuthenticatorService_PortType {
  private String _endpoint = null;
  private AuthenticatorService_PortType authenticatorService_PortType = null;
  
  public AuthenticatorServiceProxy() {
    _initAuthenticatorServiceProxy();
  }
  
  public AuthenticatorServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initAuthenticatorServiceProxy();
  }
  
  private void _initAuthenticatorServiceProxy() {
    try {
      authenticatorService_PortType = (new AuthenticatorService_ServiceLocator()).getauthenticatorServicePort();
      if (authenticatorService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)authenticatorService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)authenticatorService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (authenticatorService_PortType != null)
      ((javax.xml.rpc.Stub)authenticatorService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public AuthenticatorService_PortType getAuthenticatorService_PortType() {
    if (authenticatorService_PortType == null)
      _initAuthenticatorServiceProxy();
    return authenticatorService_PortType;
  }
  
  public Response getUserWorkspaces(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (authenticatorService_PortType == null)
      _initAuthenticatorServiceProxy();
    return authenticatorService_PortType.getUserWorkspaces(username, password);
  }
  
  public Response login(java.lang.String username, java.lang.String password, int workspaceId) throws java.rmi.RemoteException{
    if (authenticatorService_PortType == null)
      _initAuthenticatorServiceProxy();
    return authenticatorService_PortType.login(username, password, workspaceId);
  }
  
  
}