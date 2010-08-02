/**
 * GetSubspaceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs;

public class GetSubspaceResponse  implements java.io.Serializable {
    private de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference subspaceReference;

    public GetSubspaceResponse() {
    }

    public GetSubspaceResponse(
           de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference subspaceReference) {
           this.subspaceReference = subspaceReference;
    }


    /**
     * Gets the subspaceReference value for this GetSubspaceResponse.
     * 
     * @return subspaceReference
     */
    public de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference getSubspaceReference() {
        return subspaceReference;
    }


    /**
     * Sets the subspaceReference value for this GetSubspaceResponse.
     * 
     * @param subspaceReference
     */
    public void setSubspaceReference(de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference subspaceReference) {
        this.subspaceReference = subspaceReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubspaceResponse)) return false;
        GetSubspaceResponse other = (GetSubspaceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subspaceReference==null && other.getSubspaceReference()==null) || 
             (this.subspaceReference!=null &&
              this.subspaceReference.equals(other.getSubspaceReference())));
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
        if (getSubspaceReference() != null) {
            _hashCode += getSubspaceReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubspaceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">GetSubspaceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subspaceReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "SubspaceReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", ">SubspaceReference"));
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
