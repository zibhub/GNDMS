/**
 * GetSupportedOfferTypesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.stubs;

public class GetSupportedOfferTypesResponse  implements java.io.Serializable {
    private org.apache.axis.types.URI[] response;

    public GetSupportedOfferTypesResponse() {
    }

    public GetSupportedOfferTypesResponse(
           org.apache.axis.types.URI[] response) {
           this.response = response;
    }


    /**
     * Gets the response value for this GetSupportedOfferTypesResponse.
     * 
     * @return response
     */
    public org.apache.axis.types.URI[] getResponse() {
        return response;
    }


    /**
     * Sets the response value for this GetSupportedOfferTypesResponse.
     * 
     * @param response
     */
    public void setResponse(org.apache.axis.types.URI[] response) {
        this.response = response;
    }

    public org.apache.axis.types.URI getResponse(int i) {
        return this.response[i];
    }

    public void setResponse(int i, org.apache.axis.types.URI _value) {
        this.response[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSupportedOfferTypesResponse)) return false;
        GetSupportedOfferTypesResponse other = (GetSupportedOfferTypesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.response==null && other.getResponse()==null) || 
             (this.response!=null &&
              java.util.Arrays.equals(this.response, other.getResponse())));
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
        if (getResponse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResponse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResponse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSupportedOfferTypesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", ">GetSupportedOfferTypesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("response");
        elemField.setXmlName(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", "response"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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
