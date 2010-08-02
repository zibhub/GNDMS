/**
 * ListCreatableSliceKindsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subspace.stubs;

public class ListCreatableSliceKindsResponse  implements java.io.Serializable {
    private org.apache.axis.types.URI[] sliceKind;

    public ListCreatableSliceKindsResponse() {
    }

    public ListCreatableSliceKindsResponse(
           org.apache.axis.types.URI[] sliceKind) {
           this.sliceKind = sliceKind;
    }


    /**
     * Gets the sliceKind value for this ListCreatableSliceKindsResponse.
     * 
     * @return sliceKind
     */
    public org.apache.axis.types.URI[] getSliceKind() {
        return sliceKind;
    }


    /**
     * Sets the sliceKind value for this ListCreatableSliceKindsResponse.
     * 
     * @param sliceKind
     */
    public void setSliceKind(org.apache.axis.types.URI[] sliceKind) {
        this.sliceKind = sliceKind;
    }

    public org.apache.axis.types.URI getSliceKind(int i) {
        return this.sliceKind[i];
    }

    public void setSliceKind(int i, org.apache.axis.types.URI _value) {
        this.sliceKind[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListCreatableSliceKindsResponse)) return false;
        ListCreatableSliceKindsResponse other = (ListCreatableSliceKindsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceKind==null && other.getSliceKind()==null) || 
             (this.sliceKind!=null &&
              java.util.Arrays.equals(this.sliceKind, other.getSliceKind())));
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
        if (getSliceKind() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSliceKind());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSliceKind(), i);
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
        new org.apache.axis.description.TypeDesc(ListCreatableSliceKindsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">ListCreatableSliceKindsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceKind");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceKind"));
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
