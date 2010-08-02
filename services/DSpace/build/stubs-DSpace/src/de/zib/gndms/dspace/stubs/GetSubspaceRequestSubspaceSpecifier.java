/**
 * GetSubspaceRequestSubspaceSpecifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs;

public class GetSubspaceRequestSubspaceSpecifier  implements java.io.Serializable {
    private javax.xml.namespace.QName subspaceSpecifier;

    public GetSubspaceRequestSubspaceSpecifier() {
    }

    public GetSubspaceRequestSubspaceSpecifier(
           javax.xml.namespace.QName subspaceSpecifier) {
           this.subspaceSpecifier = subspaceSpecifier;
    }


    /**
     * Gets the subspaceSpecifier value for this GetSubspaceRequestSubspaceSpecifier.
     * 
     * @return subspaceSpecifier
     */
    public javax.xml.namespace.QName getSubspaceSpecifier() {
        return subspaceSpecifier;
    }


    /**
     * Sets the subspaceSpecifier value for this GetSubspaceRequestSubspaceSpecifier.
     * 
     * @param subspaceSpecifier
     */
    public void setSubspaceSpecifier(javax.xml.namespace.QName subspaceSpecifier) {
        this.subspaceSpecifier = subspaceSpecifier;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubspaceRequestSubspaceSpecifier)) return false;
        GetSubspaceRequestSubspaceSpecifier other = (GetSubspaceRequestSubspaceSpecifier) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subspaceSpecifier==null && other.getSubspaceSpecifier()==null) || 
             (this.subspaceSpecifier!=null &&
              this.subspaceSpecifier.equals(other.getSubspaceSpecifier())));
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
        if (getSubspaceSpecifier() != null) {
            _hashCode += getSubspaceSpecifier().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubspaceRequestSubspaceSpecifier.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>GetSubspaceRequest>subspaceSpecifier"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subspaceSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SubspaceSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
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
