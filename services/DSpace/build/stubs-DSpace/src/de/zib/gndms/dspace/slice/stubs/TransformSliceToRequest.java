/**
 * TransformSliceToRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs;

public class TransformSliceToRequest  implements java.io.Serializable {
    private java.lang.String subspaceId;
    private de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceKind sliceKind;

    public TransformSliceToRequest() {
    }

    public TransformSliceToRequest(
           de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceKind sliceKind,
           java.lang.String subspaceId) {
           this.subspaceId = subspaceId;
           this.sliceKind = sliceKind;
    }


    /**
     * Gets the subspaceId value for this TransformSliceToRequest.
     * 
     * @return subspaceId
     */
    public java.lang.String getSubspaceId() {
        return subspaceId;
    }


    /**
     * Sets the subspaceId value for this TransformSliceToRequest.
     * 
     * @param subspaceId
     */
    public void setSubspaceId(java.lang.String subspaceId) {
        this.subspaceId = subspaceId;
    }


    /**
     * Gets the sliceKind value for this TransformSliceToRequest.
     * 
     * @return sliceKind
     */
    public de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceKind getSliceKind() {
        return sliceKind;
    }


    /**
     * Sets the sliceKind value for this TransformSliceToRequest.
     * 
     * @param sliceKind
     */
    public void setSliceKind(de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceKind sliceKind) {
        this.sliceKind = sliceKind;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransformSliceToRequest)) return false;
        TransformSliceToRequest other = (TransformSliceToRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subspaceId==null && other.getSubspaceId()==null) || 
             (this.subspaceId!=null &&
              this.subspaceId.equals(other.getSubspaceId()))) &&
            ((this.sliceKind==null && other.getSliceKind()==null) || 
             (this.sliceKind!=null &&
              this.sliceKind.equals(other.getSliceKind())));
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
        if (getSubspaceId() != null) {
            _hashCode += getSubspaceId().hashCode();
        }
        if (getSliceKind() != null) {
            _hashCode += getSliceKind().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransformSliceToRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">TransformSliceToRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subspaceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", "subspaceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceKind");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", "sliceKind"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">>TransformSliceToRequest>sliceKind"));
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
