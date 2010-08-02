/**
 * ListPublicSubspacesRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs;

public class ListPublicSubspacesRequest  implements java.io.Serializable {
    private org.apache.axis.types.URI schemaURI;

    public ListPublicSubspacesRequest() {
    }

    public ListPublicSubspacesRequest(
           org.apache.axis.types.URI schemaURI) {
           this.schemaURI = schemaURI;
    }


    /**
     * Gets the schemaURI value for this ListPublicSubspacesRequest.
     * 
     * @return schemaURI
     */
    public org.apache.axis.types.URI getSchemaURI() {
        return schemaURI;
    }


    /**
     * Sets the schemaURI value for this ListPublicSubspacesRequest.
     * 
     * @param schemaURI
     */
    public void setSchemaURI(org.apache.axis.types.URI schemaURI) {
        this.schemaURI = schemaURI;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListPublicSubspacesRequest)) return false;
        ListPublicSubspacesRequest other = (ListPublicSubspacesRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.schemaURI==null && other.getSchemaURI()==null) || 
             (this.schemaURI!=null &&
              this.schemaURI.equals(other.getSchemaURI())));
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
        if (getSchemaURI() != null) {
            _hashCode += getSchemaURI().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ListPublicSubspacesRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListPublicSubspacesRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schemaURI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "schemaURI"));
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
