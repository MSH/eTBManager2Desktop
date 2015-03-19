/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.msh.etbm.webservices.authenticator;

public class Response  implements java.io.Serializable {
	private static final long serialVersionUID = -868544445749241165L;

	public static final int RESP_SUCCESS = 0;
	public static final int RESP_AUTHENTICATION_FAIL = 1;
	public static final int RESP_INVALID_SESSION = 2;
	public static final int RESP_UNEXPECTED_ERROR = 3;
	public static final int RESP_VALIDATION_ERROR = 4;

	private java.lang.String errormsg;

    private int errorno;

    private java.lang.String result;

    public Response() {
    }

    public Response(
           java.lang.String errormsg,
           int errorno,
           java.lang.String result) {
           this.errormsg = errormsg;
           this.errorno = errorno;
           this.result = result;
    }


    /**
     * Gets the errormsg value for this Response.
     * 
     * @return errormsg
     */
    public java.lang.String getErrormsg() {
        return errormsg;
    }


    /**
     * Sets the errormsg value for this Response.
     * 
     * @param errormsg
     */
    public void setErrormsg(java.lang.String errormsg) {
        this.errormsg = errormsg;
    }


    /**
     * Gets the errorno value for this Response.
     * 
     * @return errorno
     */
    public int getErrorno() {
        return errorno;
    }


    /**
     * Sets the errorno value for this Response.
     * 
     * @param errorno
     */
    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }


    /**
     * Gets the result value for this Response.
     * 
     * @return result
     */
    public java.lang.String getResult() {
        return result;
    }


    /**
     * Sets the result value for this Response.
     * 
     * @param result
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errormsg==null && other.getErrormsg()==null) || 
             (this.errormsg!=null &&
              this.errormsg.equals(other.getErrormsg()))) &&
            this.errorno == other.getErrorno() &&
            ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              this.result.equals(other.getResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getErrormsg() != null) {
            _hashCode += getErrormsg().hashCode();
        }
        _hashCode += getErrorno();
        if (getResult() != null) {
            _hashCode += getResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://etbmanager.org/authenticator", "response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errormsg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errormsg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
